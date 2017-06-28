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

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;

import br.com.leonardoz.dsl.ConnectionFactory;
import br.com.leonardoz.dsl.JdbcDsl;
import br.com.leonardoz.models.Todo;
import br.com.leonardoz.models.User;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class JdbcDslUpdateTest extends JdbcDslBaseTest {

	@Test
	public void shouldUpdateAllUsers() throws DataSetException {
		ConnectionFactory factory = factory();
		int affected = JdbcDsl.sql(factory)
			.statement("UPDATE USERS SET NAME = NAME + '-SAN'")
			.asDml()
			.execute();
		
		assertEquals(3, affected);
		
		List<User> selectedUsers = JdbcDsl
				.sql(factory)
				.statement("SELECT ID, NAME FROM USERS")
				.asQuery(userParser)
				.getAll();
		
		Set<Boolean> tests = new HashSet<>();
		for (User user : selectedUsers) {
			String name = user.getName();
			boolean contains = name.contains("-SAN");
			tests.add(contains);
		}
		
		assertFalse(tests.contains(Boolean.FALSE));
	}
	
	@Test
	public void shouldUpdateATodo() throws DataSetException {
		ConnectionFactory factory = factory();
		int affected = JdbcDsl.sql(factory)
			.statement("UPDATE TODOS SET DESCRIPTION = DESCRIPTION + '-TTEBAYO' WHERE ID = ?")
			.withParameters(4)
			.asDml()
			.execute();
		
		assertEquals(1, affected);
		
		Todo selectedTodo = JdbcDsl
				.sql(factory())
				.statement("SELECT ID, TITLE, DESCRIPTION, USER_ID FROM TODOS WHERE ID = ?")
				.withParameters(4)
				.asQuery(todoParser)
				.get();
		
		assertTrue(selectedTodo.getDescription().contains("-TTEBAYO"));
	}

}
