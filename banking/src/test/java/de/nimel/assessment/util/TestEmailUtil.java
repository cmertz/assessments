package de.nimel.assessment.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.nimel.assessment.util.EmailUtil;

public final class TestEmailUtil {

	@Test(groups = "unit")
	public void testGoodEmailAddress() {
		Assert.assertTrue(EmailUtil.isValidEmailAddress("chris@nimel.de"));
	}

	@Test(groups = "unit")
	public void testMalformedEmailAddress() {
		Assert.assertFalse(EmailUtil.isValidEmailAddress("ä"));
		Assert.assertFalse(EmailUtil.isValidEmailAddress("+"));
		Assert.assertFalse(EmailUtil.isValidEmailAddress("@"));
		Assert.assertFalse(EmailUtil.isValidEmailAddress("."));
	}

	@Test(groups = "unit")
	public void testEmptyEmailAddress() {
		Assert.assertFalse(EmailUtil.isValidEmailAddress(""));
	}

	@Test(groups = "unit", expectedExceptions = NullPointerException.class)
	public void testNullEmailAddress() {
		EmailUtil.isValidEmailAddress(null);
	}
}
