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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import br.com.leonardoz.dsl.query.SqlQueryExecutor;
import br.com.leonardoz.functional.JdbcDslBaseTest;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class SqlQueryExecutorTest extends JdbcDslBaseTest {

	/**
	 * Test method for {@link br.com.leonardoz.dsl.query.SqlQueryExecutor#get(java.sql.PreparedStatement, java.sql.Connection)}.
	 * @throws SQLException 
	 */
	@Test
	public void testGet() throws SQLException {
		Connection connection = factory().getConnection();
		String sql = "SELECT ID, NAME FROM USERS ORDER BY ID";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		SqlQueryExecutor executor = new SqlQueryExecutor();
		ResultSet resultSet = executor.get(prepareStatement, connection);
		assertNotNull(resultSet);
		assertTrue(resultSet.next());
		assertEquals(1, resultSet.getInt(1));
		assertEquals("SOPHIE", resultSet.getString(2));
	}

}
