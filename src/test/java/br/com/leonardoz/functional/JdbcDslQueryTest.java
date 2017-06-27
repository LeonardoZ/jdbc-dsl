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

import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;

import br.com.leonardoz.ConnectionFactory;
import br.com.leonardoz.JdbcDsl;
import br.com.leonardoz.models.Todo;
import br.com.leonardoz.models.User;

public class JdbcDslQueryTest extends JdbcDslBaseTest {

	@Test
	public void shouldReturnAllUsers() throws DataSetException {
		ConnectionFactory factory = factory();
		List<User> selectedUsers = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		
		ITable usersTable = getDataSet().getTable("USERS");
		int rowCount = usersTable.getRowCount();
		Object value = usersTable.getValue(1, "ID");
		assertEquals(rowCount, selectedUsers.size());
		assertEquals(Integer.valueOf((String) value), Integer.valueOf(selectedUsers.get(1).getId()));
	}
	
	@Test
	public void shouldReturnUserWithId2() throws DataSetException {
		ConnectionFactory factory = factory();
		User selectedUser = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS WHERE ID = ?")
				.withParameters(2)
				.asQuery(userParser)
				.get();
		
		assertEquals(2, selectedUser.getId());
	}
	
	@Test
	public void shouldReturnTodosOfUserWithId1() throws DataSetException {
		List<Todo> selectedTodos = JdbcDsl
				.sql(factory())
				.statement("SELECT ID, TITLE, DESCRIPTION, USER_ID FROM TODOS WHERE USER_ID = ?")
				.withParameters(1)
				.asQuery(todoParser)
				.getAll();
		
		assertEquals(2, selectedTodos.size());
	}

}
