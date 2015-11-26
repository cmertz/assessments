package de.nimel.assessment.util;

public final class EmailUtil {

	private static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private EmailUtil() {
		// prevent instantiation
	}

	public static boolean isValidEmailAddress(final String email) {
		return email.matches(VALID_EMAIL_PATTERN);
	}
}
