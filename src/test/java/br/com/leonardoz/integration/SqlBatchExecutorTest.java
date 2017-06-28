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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import br.com.leonardoz.dsl.batch.SqlBatchExecutor;
import br.com.leonardoz.dsl.statement.PreparedStatementUtils;
import br.com.leonardoz.functional.JdbcDslBaseTest;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class SqlBatchExecutorTest extends JdbcDslBaseTest {

	/**
	 * Test method for {@link br.com.leonardoz.dsl.batch.SqlBatchExecutor#exec(java.sql.PreparedStatement, java.sql.Connection)}.
	 * @throws SQLException       Invalid operation executed 
	 */
	@Test
	public void shouldExecCorrectly() throws SQLException {
		Connection connection = factory().getConnection();
		String sql = "INSERT INTO USERS VALUES (?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		PreparedStatementUtils.setParementers(preparedStatement, 101, "Bulbasaur");
		preparedStatement.addBatch();

		PreparedStatementUtils.setParementers(preparedStatement, 102, "Charmander");
		preparedStatement.addBatch();
		
		PreparedStatementUtils.setParementers(preparedStatement, 103, "Sqirtle");
		preparedStatement.addBatch();

		SqlBatchExecutor sqlBatchExecutor = new SqlBatchExecutor();
		int[] affected = sqlBatchExecutor.exec(preparedStatement, connection);
		
		assertEquals(3, affected.length);
	}

}
