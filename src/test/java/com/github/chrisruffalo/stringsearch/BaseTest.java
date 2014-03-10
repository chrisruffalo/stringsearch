package com.github.chrisruffalo.stringsearch;

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;

import com.github.chrisruffalo.stringsearch.Radix;

public class BaseTest {

	protected <T> void insertAndCheck(Radix<T> radix, CharSequence key, boolean strictSize, T... items) {
		// put items
		radix.put(key, items);
		
		// get items
		Set<T> values = radix.get(key);
		
		// check that the same items are there
		Assert.assertTrue(values.containsAll(Arrays.asList(items)));

		if(strictSize) {
			Assert.assertEquals(items.length, values.size());
		}
	}
	
	protected <T> void check(Radix<T> radix, CharSequence key, boolean strictSize, T... items) {
		// get items
		Set<T> values = radix.get(key);
		
		// check that the same items are there
		Assert.assertTrue(values.containsAll(Arrays.asList(items)));
		
		if(strictSize) {
			Assert.assertEquals(items.length, values.size());
		}
	}
	
}
