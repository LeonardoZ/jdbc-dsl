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
package br.com.leonardoz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TODO - HikariCP support
 * DO NOT USE THIS IN PRODUCTION
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class DefaultConnectionFactory implements ConnectionFactory {

	private ConnectionInfo info;

	public DefaultConnectionFactory(ConnectionInfo info) {
		this.info = info;
		init();
	}

	private void init() {
		try {
			Class.forName(info.getDatabaseDriver());
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
		}
	}

	@Override
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(info.getConnectUrl(), info.getUser(), info.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Connection getTransactionalConnection() {
		try {
			Connection connection = getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
