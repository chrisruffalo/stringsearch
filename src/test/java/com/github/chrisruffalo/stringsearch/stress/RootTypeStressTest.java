package com.github.chrisruffalo.stringsearch.stress;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.util.Dictionary;
import com.github.chrisruffalo.stringsearch.util.Generator;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultByteArrayNodeFactory;

public abstract class RootTypeStressTest {

	public abstract <T> Radix<T> create();
	
	public abstract int keyLength();
	
	public abstract int largeSet();
	
	public abstract int smallSet();
	
	@Test
	public void testLoadDictionary() {
		//System.out.println("Loading to list... ");
		List<String> dictionary = Dictionary.load();		
		
		//System.out.println("Loading to radix... ");
		Radix<String> radix = this.create();
		Dictionary.load(radix, true);
		
		// check
		for(String item : dictionary) {
			Set<String> result = radix.get(item);
			Assert.assertEquals(1, result.size());
			Assert.assertTrue(result.contains(String.valueOf(item.hashCode())));
		}	
		
	}
	
	@Test
	public void testLargeLoad() {
		final int ENTRIES = this.largeSet();
		final int LENGTH = this.keyLength();
		
		Radix<Integer> radix = this.create();
		
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
		final int ENTRIES = this.smallSet();
		final int LENGTH = this.keyLength();
		
		Radix<Integer> radix = this.create();
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
