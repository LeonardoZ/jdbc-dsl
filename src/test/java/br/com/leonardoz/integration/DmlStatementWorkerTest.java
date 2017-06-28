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

import java.sql.SQLException;

import org.junit.Test;

import br.com.leonardoz.dsl.dml.DmlStatementWorker;
import br.com.leonardoz.dsl.statement.SimpleStatement;
import br.com.leonardoz.functional.JdbcDslBaseTest;

/**
 * @author Leonardo H. Zapparoli 2017-06-27
 */
public class DmlStatementWorkerTest extends JdbcDslBaseTest {
	/**
	 * Test method for
	 * {@link br.com.leonardoz.dsl.dml.DmlStatementWorker#execute()}.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void shouldExecuteCorrectly() throws SQLException {
		String sql = "INSERT INTO USERS VALUES (?, ?)";
		
		SimpleStatement statement = new SimpleStatement(sql, true, 999, "Jon Doe");
		DmlStatementWorker worker = new DmlStatementWorker(statement, factory());
		
		int affected = worker.execute();
		assertEquals(1, affected);
	}

}
