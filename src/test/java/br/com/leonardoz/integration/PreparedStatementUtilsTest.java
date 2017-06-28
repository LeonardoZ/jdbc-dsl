package br.com.leonardoz.integration;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.junit.Test;

import br.com.leonardoz.dsl.statement.PreparedStatementUtils;
import br.com.leonardoz.functional.JdbcDslBaseTest;

public class PreparedStatementUtilsTest extends JdbcDslBaseTest {

	/**
	 * @throws SQLException
	 */
	@Test
	public void shouldSetParametersCorrectly() throws SQLException {
		Connection connection = directFactory().getConnection();
		PreparedStatement statement = connection.prepareStatement("INSERT INTO USERS VALUES (?, ?)");
		PreparedStatementUtils.setParementers(statement, 899, "Duono");
		ParameterMetaData parameterMetaData = statement.getParameterMetaData();
		assertEquals(2, parameterMetaData.getParameterCount());
		assertEquals(Types.INTEGER, parameterMetaData.getParameterType(1));
		assertEquals(Types.VARCHAR, parameterMetaData.getParameterType(2));
	}

}
