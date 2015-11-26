package de.nimel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public final class IntersectionTest {

	@Test
	public void testDestruction() {
		Set<Integer> set1 = new HashSet<Integer>(Arrays.asList(new Integer[] {
				1, 2, 3, 4, 5 }));
		Set<Integer> set2 = new HashSet<Integer>(Arrays.asList(new Integer[] {
				1, 3, 4 }));
		SetUtil.intersection(set1, set2);
		Assert.assertEquals(
				new HashSet<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4,
						5 })), set1);
		Assert.assertEquals(
				new HashSet<Integer>(Arrays.asList(new Integer[] { 1, 3, 4 })),
				set2);
	}

	@Test
	public void testIntersection() {
		Set<Integer> set1 = new HashSet<Integer>(Arrays.asList(new Integer[] {
				1, 2, 3, 4, 5 }));
		Set<Integer> set2 = new HashSet<Integer>(Arrays.asList(new Integer[] {
				1, 3, 4 }));
		Set<Integer> res = SetUtil.intersection(set1, set2);
		Assert.assertEquals(set2, res);
	}

	@Test
	public void testSame() {
		Set<Integer> set = new HashSet<Integer>(Arrays.asList(new Integer[] {
				1, 2, 3, 4, 5 }));
		Set<Integer> res = SetUtil.intersection(set, set);
		Assert.assertEquals(set, res);
		Assert.assertNotSame(set, res);
	}
	

}