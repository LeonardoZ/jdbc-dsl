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

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;

import br.com.leonardoz.ConnectionFactory;
import br.com.leonardoz.JdbcDsl;
import br.com.leonardoz.dsl.internals.transactional.JdbcTransactionalDsl;
import br.com.leonardoz.dsl.internals.transactional.TransactionArea;
import br.com.leonardoz.models.Todo;
import br.com.leonardoz.models.User;

public class JdbcDslTransactionTest extends JdbcDslBaseTest {

	@Test
	public void shouldDeleteUserAndTodosOnTransaction() throws DataSetException {
		JdbcDsl.transaction(factory()).doInTransaction(new TransactionArea() {
				@Override
				public void interactWithDatabase(Connection connection, JdbcTransactionalDsl dsl) throws SQLException {
					dsl.dml("DELETE FROM TODOS WHERE USER_ID = ?", 1);
					dsl.dml("DELETE FROM USERS WHERE ID = ?", 1);
				}
			});
		
		List<User> selectedUsers = JdbcDsl
				.sql(factory())
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		assertEquals(2, selectedUsers.size());
		
		List<Todo> selectedTodos = JdbcDsl
				.sql(factory())
				.statement("SELECT ID, TITLE, DESCRIPTION, USER_ID FROM TODOS")
				.asQuery(todoParser)
				.getAll();
		
		assertEquals(2, selectedTodos.size());
	}
	
	
	@Test
	public void shouldNotInsertUsersOnTransactionFailed() throws DataSetException {
		ConnectionFactory factory = factory();
		JdbcDsl.transaction(factory)
			.doInTransaction(new TransactionArea() {
				@Override
				public void interactWithDatabase(Connection connection, JdbcTransactionalDsl dsl) throws SQLException {
					dsl.dml("INSERT INTO USERS VALUES(?, ?)", 62, "RICK");
					dsl.dml("INSERT INTO USERS VALUES(?, ?)", 14, "MORTY");
					dsl.dml("INSERT INTO USERaaaS VALUES(?, ?)", 62, "RICK");
				}
			});
		
		List<User> selectedUsers = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		
		assertEquals(3, selectedUsers.size());
	}
	
	@Test
	public void shouldNotInsertUsersOnTransactionGetFailed() throws DataSetException {
		
		ConnectionFactory factory = factory();
		JdbcDsl.transaction(factory)
			.doInTransaction(new TransactionArea() {
				
				@Override
				public void interactWithDatabase(Connection connection, JdbcTransactionalDsl dsl) throws SQLException {
					User user = dsl.get("SELECT ID, NAME FROM USERS WHERE id = ?", userParser, 1);
					Todo todo = new Todo();
					todo.setId(66);
					todo.setTitle("Kids");
					todo.setDescription("Leave the kinds on school.");
					todo.setUserId(user.getId());
					dsl.dml("INSERT INTO TODOS VALUES(?, ?, ?, ?)", 
							todo.getId(), 
							todo.getTitle(), 
							todo.getDescription(),
							todo.getUserId());
				}
			});
		
		List<Todo> selectedTodos = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, TITLE, DESCRIPTION, USER_ID FROM TODOS")
				.asQuery(todoParser)
				.getAll();
		assertEquals(5, selectedTodos.size());
	}
	
	@Test
	public void shouldInsertUsersOnTransaction() throws DataSetException {
		
		ConnectionFactory factory = factory();
		JdbcDsl.transaction(factory)
			.doInTransaction(new TransactionArea() {
				@Override
				public void interactWithDatabase(Connection connection, JdbcTransactionalDsl dsl) throws SQLException {
					dsl.dml("INSERT INTO USERS VALUES(?, ?)", 14, "MORTY");
					dsl.dml("INSERT INTO USERS VALUES(?, ?)", 62, "RICK");
				}
			});
		List<User> selectedUsers = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		
		assertEquals(5, selectedUsers.size());
	}
	

}
