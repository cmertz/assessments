package de.nimel.assessment.http;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;
import de.nimel.assessment.transaction.Transactions;
import de.nimel.assessment.xml.TransactionXmlAdapter;

@Service
@Path("trans")
@Produces("text/plain")
public final class TransactionsHandler {

	private Transactions transactions;

	@Autowired
	public void setTransactions(final Transactions trans) {
		this.transactions = trans;
	}

	@POST
	@Path("create")
	public void createTransaction(@QueryParam("barcode") final String barcode,
			@QueryParam("amount") final int amount,
			@QueryParam("clientEmail") final String clientEmail) {

		final Transaction trans = new Transaction(barcode, amount, clientEmail);
		transactions.addTransaction(trans);
	}

	@POST
	@Path("pay")
	public void payTransaction(@QueryParam("barcode") final String barcode,
			@QueryParam("partnerId") final String partner) {

		final Transaction trans = transactions.getTransaction(barcode);

		final Transaction newTrans = new Transaction(trans.getBarcode(),
				trans.getAmount(), trans.getClientEmail(), partner,
				TransactionState.DONE, trans.getTransactionTimestamp());

		transactions.replaceTransaction(newTrans);
	}

	@POST
	@Path("purge")
	public void purgeTransactions() {
		transactions.purgeTransactions();
	}

	@POST
	@Path("list")
	public String listTransactions() {

		StringBuilder sb = new StringBuilder();

		for (Transaction trans : transactions.getTransactions()) {

			sb.append(trans.toString());
			sb.append('\n');
		}

		return sb.toString();
	}

	@GET
	@Path("read")
	public String getTransaction(@QueryParam("barcode") final String barcode) {
		return transactions.getTransaction(barcode).toString();
	}

	@GET
	@Path("read-xml")
	@Produces("application/xml")
	public String getTransactionAsXml(
			@QueryParam("barcode") final String barcode) {
		return TransactionXmlAdapter
				.toXml(transactions.getTransaction(barcode));
	}
}
