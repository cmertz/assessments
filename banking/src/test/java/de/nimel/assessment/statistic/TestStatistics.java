package de.nimel.assessment.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.nimel.assessment.statistic.filter.DummyFilter;
import de.nimel.assessment.statistic.filter.StateFilter;
import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;
import de.nimel.assessment.transaction.Transactions;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public final class TestStatistics extends AbstractTestNGSpringContextTests {

	private Transactions transactions;

	private Statistics statistics;

	@Autowired
	public void setStatistics(final Statistics statistics) {
		this.statistics = statistics;
	}

	@Autowired
	public void setTransactions(final Transactions trans) {
		this.transactions = trans;
	}

	private final void assertPercentages(final int pending, final int done,
			final int cancelled) {

		final int pendingPercentage = statistics
				.getTransactionPercentage(new StateFilter(
						TransactionState.PENDING));
		final int donePercentage = statistics
				.getTransactionPercentage(new StateFilter(TransactionState.DONE));
		final int cancelledPercentage = statistics
				.getTransactionPercentage(new StateFilter(
						TransactionState.CANCELLED));

		Assert.assertTrue(pending == pendingPercentage);
		Assert.assertTrue(done == donePercentage);
		Assert.assertTrue(cancelled == cancelledPercentage);
	}

	private final void assertTotal(final int total) {
		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == total);
	}

	@Test(groups = "unit")
	public void testPercentages() {

		transactions.purgeTransactions();

		assertTotal(0);

		transactions.addTransaction(new Transaction("abcdefghijklm", 100,
				"chris@nimel.de"));

		assertTotal(1);
		assertPercentages(100, 0, 0);

		transactions.replaceTransaction(new Transaction("abcdefghijklm", 100,
				"chris@nimel.de", "netto", TransactionState.DONE, System
						.currentTimeMillis()));

		assertTotal(1);
		assertPercentages(0, 100, 0);

		transactions.addTransaction(new Transaction("bcdefghijklmn", 100,
				"chris@nimel.de"));

		assertTotal(2);
		assertPercentages(50, 50, 0);
	}
}
