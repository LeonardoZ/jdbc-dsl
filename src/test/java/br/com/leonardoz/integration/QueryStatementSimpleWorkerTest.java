/**
MIT License

Copyright (c) [2017] [Leonardo Henrique Zapparoli]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package br.com.leonardoz.integration;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;

import br.com.leonardoz.dsl.query.QueryStatementSimpleWorker;
import br.com.leonardoz.dsl.statement.SimpleStatement;
import br.com.leonardoz.functional.JdbcDslBaseTest;
import br.com.leonardoz.models.User;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class QueryStatementSimpleWorkerTest extends JdbcDslBaseTest {

	/**
	 * Test method for {@link br.com.leonardoz.dsl.query.QueryStatementSimpleWorker#get()}.
	 * @throws SQLException 
	 * @throws DataSetException 
	 */
	@Test
	public void shouldGetAll() throws SQLException, DataSetException {
		String sql = "SELECT ID, NAME FROM USERS";
		SimpleStatement simpleStatement = new SimpleStatement(sql, false);
		Connection connection = factory().getConnection();
		QueryStatementSimpleWorker<User> worker = new QueryStatementSimpleWorker<>(simpleStatement, connection, userParser);
		
		List<User> selectedUsers = worker.getAll();
		ITable usersTable = getDataSet().getTable("USERS");
		
		int rowCount = usersTable.getRowCount();
		Object value = usersTable.getValue(1, "ID");
		assertEquals(rowCount, selectedUsers.size());
		assertEquals(Integer.valueOf((String) value), Integer.valueOf(selectedUsers.get(1).getId()));
	}

	/**
	 * Test method for {@link br.com.leonardoz.dsl.query.QueryStatementSimpleWorker#getAll()}.
	 * @throws SQLException 
	 */
	@Test
	public void shouldGet() throws SQLException {
		String sql = "SELECT ID, NAME FROM USERS WHERE ID = ?";
		SimpleStatement simpleStatement = new SimpleStatement(sql, false, 3);
		Connection connection = factory().getConnection();
		QueryStatementSimpleWorker<User> worker = new QueryStatementSimpleWorker<>(simpleStatement, connection, userParser);
		
		User selectedUser = worker.get();
		assertEquals(3, selectedUser.getId());
	}

}
