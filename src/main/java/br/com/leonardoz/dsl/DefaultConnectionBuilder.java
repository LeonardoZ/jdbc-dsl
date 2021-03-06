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
package br.com.leonardoz.dsl;

/**
 *  TODO
 * @author Leonardo H. Zapparoli
 *  2017-06-27
 */
public class DefaultConnectionBuilder {

	private String databaseDriver;
	private String user;
	private String password;
	private String connectUrl;

	public DefaultConnectionBuilder databaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
		return this;
	}

	public DefaultConnectionBuilder user(String user) {
		this.user = user;
		return this;
	}

	public DefaultConnectionBuilder password(String password) {
		this.password = password;
		return this;
	}

	public DefaultConnectionBuilder connectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
		return this;
	}
	
	public ConnectionFactory build() {
		ConnectionInfo connectionInfo = new ConnectionInfo();
		connectionInfo.setConnectUrl(connectUrl);
		connectionInfo.setDatabaseDriver(databaseDriver);
		connectionInfo.setPassword(password);
		connectionInfo.setUser(user);
		return new DefaultConnectionFactory(connectionInfo);
	}


}
