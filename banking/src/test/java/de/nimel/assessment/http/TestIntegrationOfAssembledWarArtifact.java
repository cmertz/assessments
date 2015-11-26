package de.nimel.assessment.http;

import static de.nimel.assessment.CustomAsserts.assertTransactionsEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;
import de.nimel.assessment.xml.TransactionXmlAdapter;

public final class TestIntegrationOfAssembledWarArtifact {

	// ------------------------------------
	// default test data
	// ------------------------------------

	public static final int DEFAULT_TRANS_AMOUNT = 100;

	public static final String DEFAULT_CLIENT_EMAIL = "chris@nimel.de";

	public static final String DEFAULT_PARTNER_ID = "netto";

	public static final String DEFAULT_BARCODE = "abcdefghijklm";

	// ------------------------------------
	// urls
	// ------------------------------------

	private static final String BASE_URL = "http://localhost:8080/";

	private static final String CREATE_URL = BASE_URL + "trans/create?";

	private static final String PAY_URL = BASE_URL + "trans/pay?";

	private static final String READ_XML_URL = BASE_URL + "trans/read-xml?";

	private static final String TOTAL_COUNT_URL = BASE_URL + "stats/totalcount";

	private static final String PURGE_URL = BASE_URL + "trans/purge";

	private static final String PENDING_PERCENTAGE_URL = BASE_URL
			+ "stats/pendingpercentage";

	private static final String DONE_PERCENTAGE_URL = BASE_URL
			+ "stats/donepercentage";

	private static final String CANCELLED_PERCENTAGE_URL = BASE_URL
			+ "stats/cancelledpercentage";

	private static final ClientFilter FILTER = new HTTPBasicAuthFilter(
			"test-user", "testtest");

	// ------------------------------------
	// helper
	// ------------------------------------

	private static String get(final String url) {

		final Client client = Client.create();

		client.addFilter(FILTER);

		return client.resource(url).get(String.class);
	}

	private static void post(final String url) {

		final Client client = Client.create();

		client.addFilter(FILTER);

		client.resource(url).post();
	}

	private static final void createTransaction(final String barcode) {
		post(CREATE_URL + "barcode=" + barcode + "&amount="
				+ DEFAULT_TRANS_AMOUNT + "&clientEmail=" + DEFAULT_CLIENT_EMAIL);
	}

	private static final void payTransaction(final String barcode) {
		post(PAY_URL + "barcode=" + barcode + "&partnerId="
				+ DEFAULT_PARTNER_ID);
	}

	private static void purgeTransactions() {
		post(PURGE_URL);
	}

	private static String readTransactionXml(final String barcode) {
		return get(READ_XML_URL + "barcode=" + barcode);
	}

	private static int getTotalCount() {
		return Integer.parseInt(get(TOTAL_COUNT_URL));
	}

	private static int getPendingPercentage() {
		return Integer.parseInt(get(PENDING_PERCENTAGE_URL));
	}

	private static int getDonePercentage() {
		return Integer.parseInt(get(DONE_PERCENTAGE_URL));
	}

	private static int getCancelledPercentage() {
		return Integer.parseInt(get(CANCELLED_PERCENTAGE_URL));
	}

	// ------------------------------------
	// custom asserts
	// ------------------------------------

	private static final void assertPercentages(final int pending,
			final int done, final int cancelled) {

		final int pendingPercentage = getPendingPercentage();
		final int donePercentage = getDonePercentage();
		final int cancelledPercentage = getCancelledPercentage();

		Assert.assertTrue(pending == pendingPercentage);
		Assert.assertTrue(done == donePercentage);
		Assert.assertTrue(cancelled == cancelledPercentage);
	}

	private static final void assertTotal(final int total) {
		Assert.assertTrue(getTotalCount() == total);
	}

	// ------------------------------------
	// Tests
	// ------------------------------------

	@Test(groups = "integration")
	public void testCreate() {

		purgeTransactions();

		assertTotal(0);

		createTransaction(DEFAULT_BARCODE);

		assertTotal(1);
		assertPercentages(100, 0, 0);

		final Transaction trans = new Transaction(DEFAULT_BARCODE,
				DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL, "none",
				TransactionState.PENDING, 1334826835L);

		final String xml = readTransactionXml(DEFAULT_BARCODE);

		assertTransactionsEquals(TransactionXmlAdapter.fromXml(xml), trans);
	}

	@Test(groups = "integration", dependsOnMethods = "testCreate")
	public void testPay() {

		payTransaction(DEFAULT_BARCODE);

		assertTotal(1);
		assertPercentages(0, 100, 0);

		final Transaction trans = new Transaction(DEFAULT_BARCODE,
				DEFAULT_TRANS_AMOUNT, DEFAULT_CLIENT_EMAIL, "netto",
				TransactionState.DONE, 1334826835L);

		final String xml = readTransactionXml(DEFAULT_BARCODE);

		assertTransactionsEquals(TransactionXmlAdapter.fromXml(xml), trans);
	}

	@Test(groups = "integration", dependsOnMethods = "testPay")
	public void testPurge() {

		assertTotal(1);
		assertPercentages(0, 100, 0);

		purgeTransactions();

		assertTotal(0);
		assertPercentages(0, 0, 0);
	}
}
