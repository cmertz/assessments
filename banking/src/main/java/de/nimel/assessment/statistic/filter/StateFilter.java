package de.nimel.assessment.statistic.filter;

import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;

public final class StateFilter implements Filter {

	private final TransactionState state;

	public StateFilter(final TransactionState state) {
		this.state = state;
	}

	@Override
	public boolean matches(final Transaction trans) {
		return trans.getState() == state;
	}
}