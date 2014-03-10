package com.github.chrisruffalo.stringsearch;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.root.RadixImpl;
import com.github.chrisruffalo.stringsearch.util.Dictionary;
import com.github.chrisruffalo.stringsearch.util.Generator;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultByteArrayNodeFactory;

public class StressTest {

	@Test
	public void testLoadDictionary() {
		//System.out.println("Loading to list... ");
		List<String> dictionary = Dictionary.load();
		
		
		//System.out.println("Loading to radix... ");
		Radix<String> radix = new RadixImpl<String>();
		Dictionary.load(radix, true);
		
		//System.out.println(radix.print());
		//System.out.println("========================================");
		//System.out.println("");
		//Measure.size(radix);
		
		// loaded dictionary size
		//Measure.size(dictionary);
		
		for(String item : dictionary) {
			Set<String> result = radix.get(item);
			Assert.assertEquals(1, result.size());
			Assert.assertTrue(result.contains(String.valueOf(item.hashCode())));
		}	
		
	}
	
	@Test
	public void testLargeLoad() {
		// just fast enough to not have to wait all day
		// but still a lot of entries
		final int ENTRIES = 1500000;

		// 52 characters is based on a long first name, last name, and middle initial plus breaking spaces
		// along with a 10 digit suffix. so the first name would be 13 characters, the middle name would be 
		// 13 characters, and the last name would be 13 characters.  There would be three spaces and a 10
		// digit code at the end.
		// like: "Christopher Marcellus Hamptovich 9192842123" (which is 43)
		final int LENGTH = 52;
		
		Radix<Integer> radix = new RadixImpl<Integer>();
		
		Set<String> values = new LinkedHashSet<String>();
		
		// pre-generate values
		long start = System.currentTimeMillis();
		for(int i = 0; i < ENTRIES; i++) {
			String key = Generator.generateKey(LENGTH, false);
			values.add(key);
		}
		long end = System.currentTimeMillis();
		System.out.printf("took %dms to generate %d keys\n", (end-start), ENTRIES);
		
		// measure time it takes to do radix
		start = System.currentTimeMillis();
		for(String key : values) {
			radix.put(key, key.hashCode());
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to insert %d items into multi-radix\n", (end-start), ENTRIES);
		
		// measure time it takes to verify each item
		start = System.currentTimeMillis();
		for(String key : values) {
			final Set<Integer> found = radix.get(key);
			Assert.assertEquals(1, found.size());
			Assert.assertTrue(found.contains(key.hashCode()));
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to find %d items in multi-radix\n", (end-start), ENTRIES);
	}
	
	@Test
	public void testLoadAgainstExternalImplementation() {
		// just fast enough to not have to wait all day
		// but still a lot of entries
		final int ENTRIES = 500000;

		// 52 characters is based on a long first name, last name, and middle initial plus breaking spaces
		// along with a 10 digit suffix. so the first name would be 13 characters, the middle name would be 
		// 13 characters, and the last name would be 13 characters.  There would be three spaces and a 10
		// digit code at the end.
		// like: "Christopher Marcellus Hamptovich 9192842123" (which is 43)
		final int LENGTH = 52;
		
		Radix<Integer> radix = new RadixImpl<Integer>();
		RadixTree<Integer> radixTree = new ConcurrentRadixTree<Integer>(new DefaultByteArrayNodeFactory());
		
		Set<String> values = new LinkedHashSet<String>();
		
		// pre-generate values
		long start = System.currentTimeMillis();
		for(int i = 0; i < ENTRIES; i++) {
			String key = Generator.generateKey(LENGTH, false);
			values.add(key);
		}
		long end = System.currentTimeMillis();
		System.out.printf("took %dms to generate %d keys\n", (end-start), ENTRIES);
		
		// measure time it takes to do radix
		start = System.currentTimeMillis();
		for(String key : values) {
			radix.put(key, key.hashCode());
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to insert %d items into multi-radix\n", (end-start), ENTRIES);
		
		// measure time it takes to verify each item
		start = System.currentTimeMillis();
		for(String key : values) {
			final Set<Integer> found = radix.get(key);
			Assert.assertEquals(1, found.size());
			Assert.assertTrue(found.contains(key.hashCode()));
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to find %d items in multi-radix\n", (end-start), ENTRIES);
		
		// measure time it takes to do radix (external)
		start = System.currentTimeMillis();
		for(String key : values) {
			radixTree.put(key, key.hashCode());
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to insert %d items into concurrent-radix (external)\n", (end-start), ENTRIES);
		
		// measure time it takes to verify each item (external)
		start = System.currentTimeMillis();
		for(String key : values) {
			Integer found = radixTree.getValueForExactKey(key);
			Assert.assertNotNull(found);
			Assert.assertEquals(key.hashCode(), found.intValue());
		}
		end = System.currentTimeMillis();
		System.out.printf("took %dms to find %d items in concurrent-radix (external)\n", (end-start), ENTRIES);
		
	}
}
