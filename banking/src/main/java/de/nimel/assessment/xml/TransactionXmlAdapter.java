package de.nimel.assessment.xml;

import com.thoughtworks.xstream.XStream;

import de.nimel.assessment.transaction.Transaction;

public final class TransactionXmlAdapter {

	private static final XStream xs = new XStream();

	static {
		xs.alias("transaction", Transaction.class);
	}

	private TransactionXmlAdapter() {
		// prevent instantiation
	}

	public static String toXml(final Transaction trans) {
		return xs.toXML(trans);
	}

	public static Transaction fromXml(final String xml) {
		return (Transaction) xs.fromXML(xml);
	}
}
