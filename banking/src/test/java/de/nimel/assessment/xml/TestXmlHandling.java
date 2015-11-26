package de.nimel.assessment.xml;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import de.nimel.assessment.transaction.Transaction;
import de.nimel.assessment.transaction.TransactionState;
import de.nimel.assessment.xml.TransactionXmlAdapter;

public final class TestXmlHandling {

	private static final Transaction JAVA_TRANS = new Transaction(
			"abcdefghijklm", 100, "chris@nimel.de", "netto",
			TransactionState.PENDING, 1334826835L);

	private static final String XML_TRANS = "<transaction>"
			+ "<barcode>abcdefghijklm</barcode>" + "<amount>100</amount>"
			+ "<clientEmail>chris@nimel.de</clientEmail>"
			+ "<partner>netto</partner>"
			+ "<transactionTimestamp>1334826835</transactionTimestamp>"
			+ "<state>PENDING</state>" + "</transaction>";

	static {
		XMLUnit.setIgnoreWhitespace(true);
	}

	@Test(groups = "unit")
	public void testToXml() throws SAXException, IOException {

		final String xml = TransactionXmlAdapter.toXml(JAVA_TRANS);

		XMLAssert.assertXMLEqual(XML_TRANS, xml);
	}

	@Test(groups = "unit")
	public void testToXmlAndBack() {

		final Transaction trans = TransactionXmlAdapter
				.fromXml(TransactionXmlAdapter.toXml(JAVA_TRANS));

		Assert.assertEquals(trans, JAVA_TRANS);
	}
}
