package de.nimel.assessment.transaction;

import org.springframework.beans.factory.annotation.Autowired;

import static de.nimel.assessment.CustomAsserts.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.nimel.assessment.statistic.Statistics;
import de.nimel.assessment.statistic.filter.DummyFilter;
import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.Transactions;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public final class TestTransactions extends AbstractTestNGSpringContextTests {

	private Transactions transactions;

	private Statistics statistics;

	@Autowired
	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}

	@Autowired
	public void setStatistics(Statistics stats) {
		this.statistics = stats;
	}

	@Test(groups = "unit", dependsOnGroups = "purge")
	public void testAdd() {

		transactions.purgeTransactions();

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 0);

		Transaction trans = new Transaction("abcdefghijklm", 100,
				"chris@nimel.de");

		transactions.addTransaction(trans);

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 1);

	}

	@Test(groups = "unit", dependsOnMethods = "testAdd")
	public void testReplace() {

		Transaction other = new Transaction("abcdefghijklm", 200,
				"chris@nimel.de");

		transactions.replaceTransaction(other);

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 1);
	}

	@Test(groups = "unit", dependsOnMethods = "testReplace")
	public void testGet() {

		Transaction trans = new Transaction("abcdefghijklm", 200,
				"chris@nimel.de");

		Transaction other = transactions.getTransaction("abcdefghijklm");

		assertTransactionsEquals(trans, other);
	}

	@Test(groups = { "unit", "purge" })
	public void testPurge() {

		transactions.purgeTransactions();

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 0);

		Transaction trans = new Transaction("abcdefghijklm", 100,
				"chris@nimel.de");

		transactions.addTransaction(trans);

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 1);

		transactions.purgeTransactions();

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 0);
	}

	@Test(groups = "unit", expectedExceptions = IllegalArgumentException.class)
	public void testAddNull() {
		transactions.addTransaction(null);
	}

	@Test(groups = "unit", expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testAdd")
	public void testDuplicateAdd() {

		Assert.assertTrue(statistics.getTransactionCount(new DummyFilter()) == 1);

		Transaction trans = new Transaction("abcdefghijklm", 100,
				"chris@nimel.de");

		transactions.addTransaction(trans);
	}

	@Test(groups = "unit", expectedExceptions = IllegalArgumentException.class)
	public void testReplaceNull() {
		transactions.replaceTransaction(null);
	}

	@Test(groups = { "unit", "purge" }, expectedExceptions = IllegalArgumentException.class)
	public void testReplaceNonExistant() {

		transactions.purgeTransactions();

		final Transaction trans = new Transaction("abcdefghijklm", 100,
				"chris@nimel.de");

		transactions.replaceTransaction(trans);
	}

	@Test(groups = "unit", expectedExceptions = IllegalArgumentException.class)
	public void testGetNull() {
		transactions.getTransaction(null);
	}
}
