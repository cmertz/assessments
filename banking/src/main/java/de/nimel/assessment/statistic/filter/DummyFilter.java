package de.nimel.assessment.statistic.filter;

import de.nimel.assessment.transaction.Transaction;

public final class DummyFilter implements Filter {

	@Override
	public boolean matches(Transaction trans) {
		return true;
	}
}
