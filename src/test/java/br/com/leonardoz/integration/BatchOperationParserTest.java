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

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.leonardoz.dsl.batch.BatchOperationParser;
import br.com.leonardoz.functional.JdbcDslBaseTest;

/**
 * @author Leonardo H. Zapparoli 2017-06-27
 */
public class BatchOperationParserTest extends JdbcDslBaseTest {


	@Test
	public void shouldParseSqlCorrectly() throws SQLException {
		Connection connection = factory().getConnection();
		String sql = "INSERT INTO USERS VALUES (?, ?)";
		
		List<Object[]> paramsOfStatement = new ArrayList<>(3);
		paramsOfStatement.add(new Object[] {78, "Articuno"});
		paramsOfStatement.add(new Object[] {79, "Montres"});
		paramsOfStatement.add(new Object[] {80, "Zapdos"});
		
		BatchOperationParser batchOperationParser = new BatchOperationParser();
		PreparedStatement statement = batchOperationParser.parse(sql, paramsOfStatement, connection);
		
		assertNotNull(statement);
		
		ParameterMetaData parameterMetaData = statement.getParameterMetaData();
		assertEquals(2, parameterMetaData.getParameterCount());
		assertEquals(Types.INTEGER, parameterMetaData.getParameterType(1));
		assertEquals(Types.VARCHAR, parameterMetaData.getParameterType(2));
	}

}
