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
package br.com.leonardoz.dsl.statement;

import br.com.leonardoz.dsl.ConnectionFactory;
import br.com.leonardoz.dsl.dml.DmlStatementWorker;
import br.com.leonardoz.dsl.query.QueryStatementWorker;
import br.com.leonardoz.dsl.query.ResultSetToEntity;

/**
 * Build a {@ SimpleStatement} into an DML or DQL Worker
 *
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class StatementBuilder {

	private ConnectionFactory factory;
	private String sql;
	private Object[] params;

	public StatementBuilder(ConnectionFactory factory) {
		this.factory = factory;
	}

	public StatementBuilder statement(String sql) {
		this.sql = sql;
		return this;
	}

	public StatementBuilder withParameters(Object... params) {
		this.params = params;
		return this;
	}

	/**
	 * @return {@ DmlStatementWorker}
	 */
	public DmlStatementWorker asDml() {
		SimpleStatement sqlOperation = new SimpleStatement(getSql(), true, getParams());
		return new DmlStatementWorker(sqlOperation, factory);
	}

	/**
	 * @param resultSetToEntity
	 * @return {@ QueryStatementWorker}
	 */
	public <T> QueryStatementWorker<T> asQuery(ResultSetToEntity<T> resultSetToEntity) {
		SimpleStatement sqlOperation = new SimpleStatement(getSql(), true, getParams());
		return new QueryStatementWorker<T>(sqlOperation, factory, resultSetToEntity);
	}

	protected ConnectionFactory getFactory() {
		return factory;
	}

	protected String getSql() {
		return sql;
	}

	protected Object[] getParams() {
		return params;
	}

}
