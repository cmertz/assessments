package de.nimel.assessment.transaction;

import de.nimel.assessment.util.EmailUtil;

/**
 * Represents a transaction.
 * 
 * <p>
 * This implementation is <strong>immutable</strong>.
 * </p>
 * 
 * @author chris
 */
public final class Transaction {

	private static final String DEFAULT_PARTNER_ID = "none";

	private final String barcode;

	private final int amount;

	private final String clientEmail;

	private final String partner;

	private final long transactionTimestamp;

	private final TransactionState state;

	public Transaction(final String barcode, final int amount,
			final String clientEmail) {

		this(barcode, amount, clientEmail, DEFAULT_PARTNER_ID,
				TransactionState.PENDING, System.currentTimeMillis());
	}

	public Transaction(final String barcode, final int amount,
			final String clientEmail, final String partner,
			final TransactionState state, final long ts) {

		if (!isValidBarcode(barcode) || clientEmail == null
				|| !EmailUtil.isValidEmailAddress(clientEmail) || amount <= 0
				|| ts <= 0) {
			throw new IllegalArgumentException();
		}

		this.barcode = barcode;
		this.amount = amount;
		this.clientEmail = clientEmail;
		this.partner = partner;
		this.state = state;
		this.transactionTimestamp = ts;
	}

	private static boolean isValidBarcode(final String barcode) {
		return barcode != null && barcode.length() == 13;
	}

	public String getBarcode() {
		return barcode;
	}

	public int getAmount() {
		return amount;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public String getPartner() {
		return partner;
	}

	public long getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public TransactionState getState() {
		return state;
	}

	@Override
	public int hashCode() {

		int res = 17;

		res = 31 * res + state.hashCode();
		res = 31 * res + clientEmail.hashCode();
		res = 31 * res + partner.hashCode();
		res = 31 * res + amount;
		res = 31 * res + barcode.hashCode();
		res = 31 * res
				+ (int) (transactionTimestamp ^ (transactionTimestamp >>> 32));

		return res;
	}

	@Override
	public boolean equals(final Object other) {

		if (this == other) {
			return true;
		}

		if (!(other instanceof Transaction)) {
			return false;
		}

		Transaction trans = (Transaction) other;

		return this.state == trans.state && this.amount == trans.amount
				&& this.clientEmail.equals(trans.clientEmail)
				&& this.barcode.equals(trans.barcode)
				&& this.partner.equals(trans.partner)
				&& this.transactionTimestamp == trans.transactionTimestamp;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("barcode: ");
		sb.append(barcode);
		sb.append(" amount: ");
		sb.append(amount);
		sb.append(" client email: ");
		sb.append(clientEmail);
		sb.append(" partner id: ");
		sb.append(partner);
		sb.append(" timestamp: ");
		sb.append(transactionTimestamp);
		sb.append(" state: ");
		sb.append(state);

		return sb.toString();
	}
}
