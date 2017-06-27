package br.com.leonardoz.functional;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DefaultConnectionFactoryTest.class, JdbcDslDeleteTest.class, JdbcDslInsertTest.class,
		JdbcDslQueryTest.class, JdbcDslTransactionTest.class, JdbcDslUpdateTest.class, JdbcDslBatchTest.class })
public class AllFunctionalTests {

}
