package de.nimel.assessment;

import de.nimel.assessment.transaction.Transaction;

public final class CustomAsserts {

	private CustomAsserts() {
		// prevent instantiation
	}

	/**
	 * custom assertion that compares without timestamps
	 * 
	 * @param one
	 * @param other
	 */
	public static final void assertTransactionsEquals(final Transaction one,
			final Transaction other) {

		if (one.equals(other)) {
			return;
		}

		if (one.getAmount() == other.getAmount()
				&& one.getBarcode().equals(other.getBarcode())
				&& one.getClientEmail().equals(other.getClientEmail())
				&& one.getPartner().equals(other.getPartner())
				&& one.getState() == other.getState()) {
			return;
		}

		throw new AssertionError();
	}
}
