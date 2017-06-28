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
package br.com.leonardoz.dsl.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.leonardoz.dsl.statement.SimpleStatement;
import br.com.leonardoz.dsl.statement.SimpleStatementParser;

/**
 * Used for Transactions
 * 
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class DmlStatementSimpleWorker {

	private SimpleStatement sqlOperation;
	private Connection connection;

	/**
	 * @param simpleStatement 	Non-empty SimpleStatement
	 * @param connection 	Connection#getAutoCommit() should returns false
	 */
	public DmlStatementSimpleWorker(SimpleStatement simpleStatement, Connection connection) {
		this.sqlOperation = simpleStatement;
		this.connection = connection;
	}

	/**
	 * @return Affected Rows
	 * @throws SQLException       Invalid operation executed
	 */
	public int execute() throws SQLException {
		try (PreparedStatement statement = new SimpleStatementParser().parse(sqlOperation, connection)) {

			int affectedRows = new SqlOperationExecutor().exec(statement, connection); 
			return affectedRows;
		}
	}

}