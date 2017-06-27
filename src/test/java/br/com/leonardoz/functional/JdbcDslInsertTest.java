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
import org.junit.Test;

import br.com.leonardoz.ConnectionFactory;
import br.com.leonardoz.JdbcDsl;
import br.com.leonardoz.models.Todo;
import br.com.leonardoz.models.User;

public class JdbcDslInsertTest extends JdbcDslBaseTest {

	@Test
	public void shouldInsertUser() throws DataSetException {
		User user = new User();
		user.setId(89);
		user.setName("Philip");
		
		ConnectionFactory factory = factory();
		JdbcDsl.sql(factory)
			.statement("INSERT INTO USERS VALUES (?, ?)")
			.withParameters(user.getId(), user.getName())
			.asDml()
			.execute();
		
		List<User> selectedUsers = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		
		assertEquals(4, selectedUsers.size());
	}		
	
	@Test
	public void shouldInsertTodo() throws DataSetException {
		Todo todo = new Todo();
		todo.setId(66);
		todo.setTitle("Kids");
		todo.setDescription("Leave the kinds on school.");
		todo.setUserId(3);
		ConnectionFactory factory = factory();
		JdbcDsl.sql(factory)
			.statement("INSERT INTO TODOS VALUES (?, ?, ?, ?)")
			.withParameters(
					todo.getId(), 
					todo.getTitle(), 
					todo.getDescription(),
					todo.getUserId())
			.asDml()
			.execute();
		
		List<Todo> selectedTodos = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, TITLE, DESCRIPTION, USER_ID FROM TODOS")
				.asQuery(todoParser)
				.getAll();
		assertEquals(5, selectedTodos.size());
	}	
	
	
}
