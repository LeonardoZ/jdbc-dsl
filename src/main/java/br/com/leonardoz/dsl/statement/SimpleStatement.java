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

/**
 * Represents a INSERT / UPDATE / DELETE / SELECT statement
 * 
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class SimpleStatement {

	private String sql;
	private Object[] parameters;
	private boolean isDml;

	/**
	 * @param sql
	 * @param isDml
	 * @param parameters
	 */
	public SimpleStatement(String sql, boolean isDml, Object... parameters) {
		this.sql = sql;
		this.isDml = isDml;
		this.parameters = parameters;
	}

	public String getSql() {
		return sql;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public boolean isDml() {
		return isDml;
	}
	

}
