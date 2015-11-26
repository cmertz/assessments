package de.nimel.assessment.transaction;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;

public final class TestTransaction {

	// ------------------------------------
	// default test data
	// ------------------------------------

	private static final int DEFAULT_TRANS_AMOUNT = 100;

	private static final String DEFAULT_CLIENT_EMAIL = "chris@nimel.de";
	
	private static final String DEFAULT_BARCODE = "abcdefghijklm";
			
	// ------------------------------------
	// tests
	// ------------------------------------
	
	@Test(groups = "unit")
	public void testNotEquals() throws InterruptedException {

		final Transaction one = new Transaction(DEFAULT_BARCODE, DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL);

		// we need a time gap between the
		// two creations to have different
		// timestamps in the transaction instances
		Thread.sleep(DEFAULT_TRANS_AMOUNT);

		final Transaction another = new Transaction(DEFAULT_BARCODE, DEFAULT_TRANS_AMOUNT,
				DEFAULT_CLIENT_EMAIL);

		Assert.assertNotEquals(one, another);
		Assert.assertNotEquals(one.hashCode(), another.hashCode());
		Assert.assertNotEquals(one.toString(), another.toString());
	}

	@Test(groups = "unit")
	public void testEquals() {

		final long ts = System.currentTimeMillis();

		Transaction one = new Transaction(DEFAULT_BARCODE, DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL,
				"netto", TransactionState.PENDING, ts);

		Transaction another = new Transaction(DEFAULT_BARCODE, DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL,
				"netto", TransactionState.PENDING, ts);

		Assert.assertEquals(one, another);
		Assert.assertEquals(one.hashCode(), another.hashCode());
		Assert.assertEquals(one.toString(), another.toString());

	}

	@Test(groups = "unit")
	public void testToString() {

		Transaction one = new Transaction(DEFAULT_BARCODE, DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL);

		final String toString = one.toString();

		Assert.assertTrue(toString.contains(DEFAULT_BARCODE));
		Assert.assertTrue(toString.contains(Integer.toString(DEFAULT_TRANS_AMOUNT)));
		Assert.assertTrue(toString.contains(DEFAULT_CLIENT_EMAIL));
		Assert.assertTrue(toString.contains("none"));
	}
}
