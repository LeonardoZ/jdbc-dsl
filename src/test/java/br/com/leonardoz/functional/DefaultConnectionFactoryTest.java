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

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.hsqldb.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.leonardoz.ConnectionFactory;
import br.com.leonardoz.DefaultConnectionBuilder;

public class DefaultConnectionFactoryTest {

	private Server server;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Before
	public void setUp() throws Exception {
		server = new Server();
		server.setDatabaseName(0, "testdb");
		server.setTrace(true);
		server.setPort(9002);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		server.shutdown();
	}

	@Test
	public void shouldConnectToDatabase() {
		ConnectionFactory connectionFactory = new DefaultConnectionBuilder()
			.connectUrl("jdbc:hsqldb:mem://localhost:9002/testdb")
			.databaseDriver("org.hsqldb.jdbcDriver")
			.user("SA")
			.password("")
			.build();
		
		Connection connection = connectionFactory.getConnection();
		try {
			assertTrue(!connection.isClosed());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void connectionShouldNotBeAutoCommit() {
		ConnectionFactory connectionFactory = new DefaultConnectionBuilder()
			.connectUrl("jdbc:hsqldb:mem://localhost:9002/testdb")
			.databaseDriver("org.hsqldb.jdbcDriver")
			.user("SA")
			.password("")
			.build();
		
		Connection connection = connectionFactory.getConnection();
		try {
			connection.setAutoCommit(false);
			assertTrue(!connection.getAutoCommit());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

}
