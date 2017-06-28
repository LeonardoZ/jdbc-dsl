/*******************************************************************************
 * Copyright (C) 2017 Leonardo Henrique Zapparoli
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package br.com.leonardoz.functional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hsqldb.server.Server;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.leonardoz.dsl.ConnectionFactory;
import br.com.leonardoz.dsl.DefaultConnectionBuilder;
import br.com.leonardoz.dsl.query.ResultSetToEntity;
import br.com.leonardoz.models.Todo;
import br.com.leonardoz.models.User;

public class JdbcDslBaseTest {
	private Server server;
	private IDatabaseTester databaseTester;
	private IDataSet dataSet;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected ResultSetToEntity<User> userParser = new ResultSetToEntity<User>() {
		@Override
		public User parse(ResultSet resultSet) throws SQLException {
			int id = resultSet.getInt(1);
			String name = resultSet.getString(2);
			User user = new User();
			user.setId(id);
			user.setName(name);
			return user;
		}
	};

	protected ResultSetToEntity<Todo> todoParser = new ResultSetToEntity<Todo>() {
		@Override
		public Todo parse(ResultSet resultSet) throws SQLException {
			int id = resultSet.getInt(1);
			String title = resultSet.getString(2);
			String description = resultSet.getString(3);
			int userId = resultSet.getInt(4);

			Todo todo = new Todo();
			todo.setId(id);
			todo.setTitle(title);
			todo.setDescription(description);
			todo.setUserId(userId);
			return todo;
		}
	};

	public Server getServer() {
		return server;
	}

	public IDataSet getDataSet() {
		return dataSet;
	}

	public IDatabaseTester getDatabaseTester() {
		return databaseTester;
	}

	@Before
	public void init() throws Exception {
		databaseTester = new JdbcDatabaseTester(org.hsqldb.jdbcDriver.class.getName(),
				"jdbc:hsqldb:mem://localhost:9002/testdb;allow_empty_batch=false", "SA", "");
		server = new Server();
		server.setDatabaseName(0, "testdb");
		server.setTrace(true);
		server.setPort(9002);
		server.start();
		createTables(databaseTester.getConnection().getConnection());
		dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder()
				.build(new InputStreamReader(JdbcDslBaseTest.class.getResourceAsStream(("/dataset.xml")))));
		databaseTester.setDataSet(dataSet);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.onSetup();
	}

	@After
	public void cleanUp() throws Exception {
		databaseTester.onTearDown();
		databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
		databaseTester = null;
		server.shutdown();
	}

	private static void createTables(Connection connection) throws SQLException, IOException {
		InputStream resourceAsStream = JdbcDslBaseTest.class.getResourceAsStream("/tables.sql");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line + "\n");
		}
		bufferedReader.close();
		String[] statements = sb.toString().split("---");
		PreparedStatement statement = connection.prepareStatement(statements[0]);
		PreparedStatement statement2 = connection.prepareStatement(statements[1]);
		statement.execute();
		statement2.execute();
		statement.close();
		statement2.close();
	}

	public ConnectionFactory factory() {
		ConnectionFactory connectionFactory = new ConnectionFactory() {

			@Override
			public Connection getTransactionalConnection() {
				try {
					Connection conn = databaseTester.getConnection().getConnection();
					conn.setAutoCommit(false);
					return conn;
				} catch (Exception e) {
					logger.error(e.getMessage());
					return null;
				}
			}

			@Override
			public Connection getConnection() {
				try {
					return databaseTester.getConnection().getConnection();
				} catch (Exception e) {
					logger.error(e.getMessage());
					return null;
				}
			}
		};
		return connectionFactory;
	}

	public ConnectionFactory directFactory() {
		ConnectionFactory connectionFactory = new DefaultConnectionBuilder()
				.connectUrl("jdbc:hsqldb:mem://localhost:9002/testdb;allow_empty_batch=true").databaseDriver("org.hsqldb.jdbcDriver")
				.user("SA").password("").build();
		return connectionFactory;
	}

}
