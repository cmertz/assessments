package de.nimel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * traded compile time type safety for a more concise implementation
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class RefactoredIntersectionTest {

	private final Set set1;

	private final Set set2;

	public RefactoredIntersectionTest(final Set<?> set1, final Set<?> set2) {
		this.set1 = Collections.unmodifiableSet(set1);
		this.set2 = Collections.unmodifiableSet(set2);
	}

	@Parameters
	public static Collection<Object[]> createInputSets() {

		List<Object[]> res = new LinkedList<Object[]>();

		Set<Integer> integerSet1 = new HashSet<Integer>(
				Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
		Set<Integer> integerSet2 = new HashSet<Integer>(
				Arrays.asList(new Integer[] { 1, 3, 4 }));

		res.add(new Object[] { integerSet1, integerSet2 });

		Set<ComplexNumber> complexSet1 = new HashSet<ComplexNumber>();

		complexSet1.add(new ComplexNumber(1, 1));
		complexSet1.add(new ComplexNumber(1, 2));
		complexSet1.add(new ComplexNumber(1, 3));
		complexSet1.add(new ComplexNumber(1, 4));
		complexSet1.add(new ComplexNumber(2, 5));

		Set<ComplexNumber> complexSet2 = new HashSet<ComplexNumber>();

		complexSet2.add(new ComplexNumber(1, 1));
		complexSet2.add(new ComplexNumber(1, 2));
		complexSet2.add(new ComplexNumber(1, 3));

		res.add(new Object[] { complexSet1, complexSet2 });

		return res;
	}

	// ----------------------------
	// test cases
	// ----------------------------

	/*
	 * removed assertions since modification of the input sets would result in
	 * an <code>UnsupportedOperationException</code> anyways (
	 * <code>Collections.unmodifiableSet()</code> wrapper in constructor)
	 */
	@Test
	public void testDestruction() {
		SetUtil.intersection(set1, set2);
	}

	@Test
	public void testIntersection() {
		Set res = SetUtil.intersection(set1, set2);
		Assert.assertEquals(set2, res);
	}

	@Test
	public void testSame() {
		Set<Integer> res = SetUtil.intersection(set1, set1);
		Assert.assertEquals(set1, res);
		Assert.assertNotSame(set1, res);
	}
}