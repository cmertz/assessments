package de.nimel;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

/**
 * Test all possible combinations of <code>null</code> input values.
 * 
 * <p>
 * Since there are only 3 possible combinations we are doing this by hand.
 * </p>
 */
public final class AddedIntersectionTest {

	private static final Set<Object> SET = Collections
			.unmodifiableSet(Collections.emptySet());

	@Test(expected = NullPointerException.class)
	public void testBothParameterNull() {
		SetUtil.intersection(null, null);
	}

	@Test(expected = NullPointerException.class)
	public void testFirstParameterNull() {
		SetUtil.intersection(null, SET);
	}

	@Test(expected = NullPointerException.class)
	public void testSecondParameterNull() {
		SetUtil.intersection(SET, null);
	}
}
