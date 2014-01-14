package com.github.chrisruffalo.searchstring.heap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.chrisruffalo.searchstring.ISearchTree;

public class SearchHeapTest {

	@Test
	public void basicTest() {
		// create required objects
		Map<Integer, Character> matcherMap = new HashMap<>(0);
		matcherMap.put(1, 'd');
		
		Map<Long, Set<String>> values = new HashMap<>();
		
		// create new (default) navigator
		HeapNavigator<String> stringNavigator = new DirectionalNavigator<>();
		
		// create adder
		HeapVisitor<String> adder = new AddingHeapVisitor<>("adder1", values);
		stringNavigator.navigate(adder, matcherMap, "abc", true);
		
		// assert insertions have happened
		Assert.assertEquals(4, matcherMap.size());
		Assert.assertEquals(1, values.size());
		
		// new adder
		adder = new AddingHeapVisitor<>("adder2", values);
		stringNavigator.navigate(adder, matcherMap, "dabc", true);

		// assert insertions have happened
		Assert.assertEquals(7, matcherMap.size());
		Assert.assertEquals(2, values.size());

		// do searches
		SearchingHeapVisitor<String> searcher = new SearchingHeapVisitor<>(values);
		stringNavigator.navigate(searcher, matcherMap, "abc", true);
		stringNavigator.navigate(searcher, matcherMap, "dabc", true);
		stringNavigator.navigate(searcher, matcherMap, "florp", true);
		
		// matcher map should not change and value map should not change
		Assert.assertEquals(7, matcherMap.size());
		Assert.assertEquals(2, values.size());
		
		// but should have results in set from searcher
		Set<String> output = searcher.found();
		Assert.assertEquals(2, output.size());
		Assert.assertTrue(output.contains("adder1"));
		Assert.assertTrue(output.contains("adder2"));	
	}
	
	@Test
	public void testTreeInterface() {
		
		// create and init tree
		ISearchTree<String> tree = new HeapSearchTree<>();
		tree.put("abc", "adder1");
		tree.put("dabc", "adder2");
		
		// do lookups
		Assert.assertEquals(1, tree.lookup("abc").size());
		Assert.assertEquals(1, tree.lookup("dabc").size());
		Assert.assertEquals(0, tree.lookup("florp").size());
		
		// find works the same with no wildcards
		Assert.assertEquals(1, tree.find("abc").size());
		Assert.assertEquals(1, tree.find("dabc").size());
		Assert.assertEquals(0, tree.find("florp").size());

	}
	
}
