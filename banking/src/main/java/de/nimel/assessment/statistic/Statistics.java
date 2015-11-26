package de.nimel.assessment.statistic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

import de.nimel.assessment.statistic.filter.Filter;
import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.Transactions;

@Component
@Singleton
public final class Statistics {

	private Transactions transactions;

	@Autowired
	public void setTransactions(final Transactions transactions) {
		this.transactions = transactions;
	}

	private static int getCountThatMatchesFilter(
			final Iterable<Transaction> transactions, final Filter filter) {

		int counter = 0;

		for (Transaction trans : transactions) {
			if (filter.matches(trans)) {
				counter++;
			}
		}

		return counter;
	}

	public int getTransactionPercentage(final Filter filter) {

		final List<Transaction> transactions = this.transactions
				.getTransactions();

		final int total = transactions.size();

		if (total == 0) {
			return 0;
		}

		return (getCountThatMatchesFilter(transactions, filter) * 100) / total;
	}

	public int getTransactionCount(final Filter filter) {
		return getCountThatMatchesFilter(transactions.getTransactions(), filter);
	}
}
