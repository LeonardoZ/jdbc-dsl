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
package br.com.leonardoz.dsl.internals.batch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.leonardoz.ConnectionFactory;

/**
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class BatchStatementBuilder {
	
	private ConnectionFactory factory;
	private String sql;
	private List<Object[]> paramsOfStatement;

	public BatchStatementBuilder(ConnectionFactory factory) {
		this.factory = factory;
		this.paramsOfStatement = new LinkedList<>();
	}

	public BatchStatementBuilder statement(String sql) {
		this.sql = sql;
		return this;
	}

	public BatchStatementBuilder addEntry(Object... params) {
		this.paramsOfStatement.add(params);
		return this;
	}
	
	public List<Object[]> getParameters() {
		return new ArrayList<>(this.paramsOfStatement);
	}
	
	/**
	 * @return  {@link BatchStatementWorker}
	 */
	public BatchStatementWorker build() {
		BatchStatement batchStatement = new BatchStatement(sql, paramsOfStatement);
		return new BatchStatementWorker(batchStatement, factory);
	}

	protected ConnectionFactory getFactory() {
		return factory;
	}

	protected String getSql() {
		return sql;
	}

	protected List<Object[]> getParamsOfStatement() {
		return paramsOfStatement;
	}
}
