package de.nimel.assessment.statistic.filter;

import de.nimel.assessment.transaction.Transaction;

public interface Filter {
	boolean matches(Transaction trans);
}