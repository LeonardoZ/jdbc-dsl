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
package br.com.leonardoz.unit;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

import org.junit.Test;

import br.com.leonardoz.ConnectionFactory;
import br.com.leonardoz.dsl.internals.StatementBuilder;
import br.com.leonardoz.dsl.internals.dml.DmlStatementWorker;
import br.com.leonardoz.dsl.internals.query.QueryStatementWorker;
import br.com.leonardoz.dsl.internals.query.ResultSetToEntity;
import br.com.leonardoz.models.User;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class StatementBuilderTest {

	@Test
	public void shouldBuildAsDml() {
		ConnectionFactory connectionFactory = any(ConnectionFactory.class);
		String sql = "INSERT INTO USERS VALUES (?, ?)";
		DmlStatementWorker asDml = new StatementBuilder(connectionFactory)
			.statement(sql)
			.withParameters(22, "Sponger Bob")
			.asDml();
		
		assertNotNull(asDml);
	}
	
	@Test
	public void shouldBuildAsQuery() {
		ConnectionFactory connectionFactory = any(ConnectionFactory.class);
		@SuppressWarnings("unchecked")
		ResultSetToEntity<User> parser = any(ResultSetToEntity.class);
		String sql = "SELECT * FROM USERS WHERE ID = ?";
		QueryStatementWorker<User> asQuery = new StatementBuilder(connectionFactory)
			.statement(sql)
			.withParameters(22)
			.asQuery(parser);
		
		assertNotNull(asQuery);
	}


}
