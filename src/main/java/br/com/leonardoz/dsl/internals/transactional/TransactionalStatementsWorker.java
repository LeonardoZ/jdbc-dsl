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
package br.com.leonardoz.dsl.internals.transactional;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.leonardoz.ConnectionFactory;

public class TransactionalStatementsWorker {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private ConnectionFactory factory;
	
	public TransactionalStatementsWorker(ConnectionFactory factory) {
		super();
		this.factory = factory;
	}

	public void scope(TransactionArea transactionArea) {
		Connection connection = null;
		try {
			connection = factory.getTransactionalConnection();
			JdbcTransactionalDsl dsl = new JdbcTransactionalDsl(connection);
			transactionArea.interactWithDatabase(connection, dsl);
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			try {
				if (!connection.isClosed()) {
					connection.rollback();
				}
			} catch (SQLException e1){
				logger.error(e1.getMessage());
			}
		} finally {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}

	

}
