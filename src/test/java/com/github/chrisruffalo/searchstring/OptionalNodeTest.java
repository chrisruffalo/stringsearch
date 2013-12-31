package com.github.chrisruffalo.searchstring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalNodeTest extends AbstractTernaryTestCase {

	@Test
	public void testBasicOptional() {
		// create test logger
		Logger logger = LoggerFactory.getLogger(this.getClass());
		
		SearchTree<String> test = new SearchTree<>();
		
		Assert.assertTrue(test.configuration().optional().contains('?'));
		
		Seed[] seeds = new Seed[]{
			new Seed("#?", "duo-1"),
			new Seed("a?c", "two"),
			new Seed("?bc", "one"),		
			new Seed("ab?", "two-two"),
			new Seed("abc", "thr"),
			new Seed("efg", "for"),
			new Seed("afg", "fiv"),
			new Seed("bfg", "six"),
			new Seed("#?", "duo-2"), // deliberate duplicate of duo-1, testing insert
			new Seed("??", "duo-3"),
			new Seed("z?????z", "zman")
		};
		
		// rotate the seed list to make sure that
		// insertion order doesn't matter in terms
		// of the different ways that the search
		// works
		for(int i = 0; i < seeds.length; i++) {
			logger.trace("rotation: {}", i);
			
			// seed tree
			this.seed(test, seeds);
			
			// help with debug
			//test.print();
			
			// do the usual exact matches
			this.check(test, 1, "?bc", true, "one");
			this.check(test, 1, "a?c", true, "two");
			this.check(test, 1, "abc", true, "thr");
			this.check(test, 1, "ab?", true, "two-two");
			this.check(test, 1, "??", true);
			
			// not expecting matches not in that group
			this.check(test, 0, "aaa", true);
			this.check(test, 0, "?bcd", true);
			this.check(test, 0, "?bb", true);
			this.check(test, 0, "def", true);
			this.check(test, 0, "???", true);
			this.check(test, 0, "?b", true);
			this.check(test, 0, "abcdefg", true);
			
			// expecting some basic other matches
			this.check(test, 4, "abc", false, "one", "two", "two-two", "thr");
			this.check(test, 1, "ebc", false, "one");
			this.check(test, 1, "jbc", false, "one");
			this.check(test, 1, "Xbc", false, "one");
			this.check(test, 1, "dbc", false, "one");
			
			// ? acts as a literal but should be matched by 'optional'
			this.check(test, 1, "a?c", true, "two");
			
			// checking mid-stream ?
			this.check(test, 1, "aec", false, "two");
			this.check(test, 1, "aXc", false, "two");
			this.check(test, 1, "a4c", false, "two");
			this.check(test, 0, "d4c", false);
			
			// testing stacked ?s
			this.check(test, 4, "zz", false, "duo-1", "duo-2", "duo-3", "zman");
			this.check(test, 1, "zXz", false, "zman");
			this.check(test, 1, "zXXz", false, "zman");
			this.check(test, 1, "zXXXz", false, "zman");
			this.check(test, 1, "zXXXXz", false, "zman");
			this.check(test, 1, "zXXXXXz", false, "zman");
			this.check(test, 0, "zXXXXXXz", false);
			
			// exact searches with stacked ?'s
			this.check(test, 0, "zXXXXXz", true);
			this.check(test, 1, "z?????z", true, "zman");
			this.check(test, 0, "z????z", true);
			this.check(test, 0, "z???z", true);
			this.check(test, 0, "z??z", true);
			this.check(test, 0, "z?z", true);
			this.check(test, 0, "zz", true);		
			
			// checking end optional
			this.check(test, 4, "ab", false, "duo-1", "duo-2", "two-two", "duo-3");
			this.check(test, 3, "d", false, "duo-1", "duo-2", "duo-3");
			
			// rotate
			List<Seed> temp = Arrays.asList(seeds);
			Collections.rotate(temp, 1);
			seeds = temp.toArray(new Seed[temp.size()]);
			
			// recreate tree
			test = new SearchTree<>();
		}
				
	}

	/**
	 * Very specific test for the case where
	 * for some reason the optional nodes were
	 * forwarding on searches in exact mode
	 * when they weren't supposed to
	 * 
	 */
	@Test
	public void testRepeatingExactWithOptional() {
		SearchTree<String> test = new SearchTree<>();
		test.put("z?????z", "zman");
		
		//test.print();
		
		// exact searches with stacked ?'s
		this.check(test, 0, "zXXXXXz", true);
		this.check(test, 1, "z?????z", true, "zman");
		this.check(test, 0, "z????z", true);
		this.check(test, 0, "z???z", true);
		this.check(test, 0, "z??z", true);
		this.check(test, 0, "z?z", true);
		this.check(test, 0, "zz", true);		
	}

	
}
