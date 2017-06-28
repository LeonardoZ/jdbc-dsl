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
package br.com.leonardoz.dsl.transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.leonardoz.dsl.batch.BatchStatement;
import br.com.leonardoz.dsl.batch.BatchStatementSimpleWorker;
import br.com.leonardoz.dsl.dml.DmlStatementSimpleWorker;
import br.com.leonardoz.dsl.query.QueryStatementSimpleWorker;
import br.com.leonardoz.dsl.query.ResultSetToEntity;
import br.com.leonardoz.dsl.statement.SimpleStatement;

/**
 * Use this class inside 
 * {@ br.com.leonardoz.dsl.internals.transactional.TransactionArea#interactWithDatabase(Connection, JdbcTransactionalDsl) }
 * for executing statements
 * 
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class JdbcTransactionalDsl {

	private Connection connection;

	public JdbcTransactionalDsl(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Used for INSERT / UPDATE / DELETE
	 * 
	 * @param sql
	 * @param parameters
	 * @return Affected Rows
	 * @throws SQLException
	 */
	public int dml(String sql, Object... parameters) throws SQLException {
		SimpleStatement statement = new SimpleStatement(sql, true, parameters);
		return new DmlStatementSimpleWorker(statement, connection).execute();
	}

	/**
	 * Used for SELECT returning a single row
	 * 
	 * @param sql
	 * @param resultSetToEntity
	 * @param parameters
	 * @return <T>
	 * @throws SQLException
	 */
	public <T> T get(String sql, ResultSetToEntity<T> resultSetToEntity, Object... parameters)
			throws SQLException {
		SimpleStatement statement = new SimpleStatement(sql, false, parameters);
		return new QueryStatementSimpleWorker<>(statement, connection, resultSetToEntity).get();
	}

	/**
	 * Used for SELECT returning several rows
	 * 
	 * @param sql
	 * @param resultSetToEntity
	 * @param parameters
	 * @return List<T>
	 * @throws SQLException
	 */
	public <T> List<T> getAll(String sql, ResultSetToEntity<T> resultSetToEntity, Object... parameters)
			throws SQLException {
		SimpleStatement statement = new SimpleStatement(sql, false, parameters);
		return new QueryStatementSimpleWorker<>(statement, connection, resultSetToEntity).getAll();
	}

	/**
	 * Used for Batch INSERTS / UPDATES
	 * 
	 * @param sql
	 * @param parameters
	 * @return Affected Rows in each statement
	 * @throws SQLException
	 */
	public int[] batch(String sql, List<Object[]> parameters) throws SQLException {
		BatchStatement statement = new BatchStatement(sql, parameters);
		return new BatchStatementSimpleWorker(statement, connection).execute();
	}

}
