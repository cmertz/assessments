package de.nimel.assessment;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import de.nimel.assessment.http.StatisticsHandler;
import de.nimel.assessment.http.TransactionsHandler;
import de.nimel.assessment.statistic.Statistics;
import de.nimel.assessment.transaction.Transactions;

public final class TestSpringContext {

	@Test
	public void testSpringContext() {

		SpringContextVerifier verifier = new SpringContextVerifier(
				new ClassPathXmlApplicationContext(
						"classpath:applicationContext.xml"));

		verifier.assertBeanOfName("partner");

		verifier.assertBeansOfType(Transactions.class, 1);
		verifier.assertBeansOfType(Statistics.class, 1);

		verifier.assertBeansOfType(StatisticsHandler.class, 1);
		verifier.assertBeansOfType(TransactionsHandler.class, 1);

		verifier.assertNoUnusedBeans();
	}
}
