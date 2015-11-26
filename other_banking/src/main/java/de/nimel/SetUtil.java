package de.nimel;

import java.util.HashSet;
import java.util.Set;

public final class SetUtil {

	private SetUtil() {
	}

	/**
	 * return the intersection of two sets.
	 * 
	 * @return a <code>java.util.HashSet</code> containing the elements found in
	 *         both input sets
	 */
	public static <E> Set<E> intersection(final Set<E> one, final Set<E> other) {

		if (other == null)
			throw new NullPointerException();
		
		final Set<E> result = new HashSet<E>(one);

		result.retainAll(other);

		return result;
	}
}
