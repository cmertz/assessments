package de.nimel.assessment.transaction;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.Singleton;

@Component
@Singleton
public final class Transactions {

	private final ConcurrentMap<String, Transaction> barcodeMapping = new ConcurrentHashMap<String, Transaction>();

	private Set<String> validPartnerIds;

	@Resource(name = "partner")
	public void setValidPartnerIds(final Set<String> validPartnerIds) {
		this.validPartnerIds = validPartnerIds;
	}

	private void validateTransaction(final Transaction trans) {

		if (trans == null) {
			throw new IllegalArgumentException();
		}

		if (!validPartnerIds.contains(trans.getPartner())) {
			throw new IllegalArgumentException();
		}
	}

	public void addTransaction(final Transaction trans) {

		validateTransaction(trans);

		Transaction other = barcodeMapping.putIfAbsent(trans.getBarcode(),
				trans);

		if (other != null) {
			throw new IllegalArgumentException("duplicate barcode");
		}
	}

	public void replaceTransaction(final Transaction trans) {

		validateTransaction(trans);

		if (barcodeMapping.replace(trans.getBarcode(), trans) == null) {
			throw new IllegalArgumentException("transaction with barcode "
					+ trans.getBarcode() + " is not present in mapping");
		}
	}

	public void purgeTransactions() {
		barcodeMapping.clear();
	}

	public Transaction getTransaction(final String barcode) {

		if (barcode == null) {
			throw new IllegalArgumentException();
		}

		return barcodeMapping.get(barcode);
	}

	/**
	 * @return a <code>java.util.List</code> that is effectively a
	 *         <code>Set</code> since the elements in it are unique. The order
	 *         depends on the <code>hashCode()</code> of the entries and is thus
	 *         undetermined. The <strong>List</strong> that does <strong>not
	 *         reflect</strong> changes made to the underlying map after the
	 *         creation (defensive copy).
	 */
	public List<Transaction> getTransactions() {
		return new LinkedList<Transaction>(barcodeMapping.values());
	}
}
