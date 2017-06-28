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
package br.com.leonardoz.dsl.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.leonardoz.dsl.ConnectionFactory;
import br.com.leonardoz.dsl.statement.SimpleStatement;
import br.com.leonardoz.dsl.statement.SimpleStatementParser;

/**
 * @author Leonardo H. Zapparoli
 * 
 * @param <T> - Row Converted to Entity type
 * 2017-06-27
 */
public class QueryStatementWorker<T> {

	private SimpleStatement sqlOperation;
	private ConnectionFactory factory;
	private ResultSetToEntity<T> resultSetToEntity;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public QueryStatementWorker(SimpleStatement sqlOperation, ConnectionFactory factory,
			ResultSetToEntity<T> resultSetToEntity) {
		this.sqlOperation = sqlOperation;
		this.factory = factory;
		this.resultSetToEntity = resultSetToEntity;
	}

	public T get() {
		try (Connection connection = factory.getConnection();
				PreparedStatement statement = new SimpleStatementParser().parse(sqlOperation, connection);
				ResultSet resultSet = new SqlQueryExecutor().get(statement, connection)) {
			resultSet.next();
			T parsed = resultSetToEntity.parse(resultSet);
			return parsed;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<T> getAll() {
		try (Connection connection = factory.getConnection();
				PreparedStatement statement = new SimpleStatementParser().parse(sqlOperation, connection);
				ResultSet resultSet = new SqlQueryExecutor().get(statement, connection)) {
			List<T> entities = new LinkedList<>();
			while (resultSet.next()) {
				entities.add(resultSetToEntity.parse(resultSet));
			}
			return entities;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}