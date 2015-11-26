package de.nimel.assessment.statistic.filter;

import de.nimel.assessment.statistic.StatisticsPeriod;
import de.nimel.assessment.transaction.Transaction;

public final class PeriodFilter implements Filter {

	private final long limit;

	public PeriodFilter(final StatisticsPeriod period) {
		limit = System.currentTimeMillis() - period.getMilliseconds();
	}

	@Override
	public boolean matches(final Transaction trans) {
		return trans.getTransactionTimestamp() >= limit;
	}
}