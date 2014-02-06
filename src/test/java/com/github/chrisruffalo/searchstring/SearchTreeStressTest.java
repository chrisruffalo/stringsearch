package com.github.chrisruffalo.searchstring;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chrisruffalo.searchstring.heap.HeapSearchTree;
import com.github.chrisruffalo.searchstring.radix.RadixSearchTree;
import com.google.common.base.Strings;

public class SearchTreeStressTest {

	// in percent
	private static final int LOG_INTERVAL = 10;
	
	private static final int SIZE = 6300000;
	
	private static final int LENGTH = 150;
	
	private static final String ALLOWED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String WILD_ALLOWED = SearchTreeStressTest.ALLOWED + "#?*";
	
	private Random random;
	
	@Before
	public void init() {
		this.random = new Random(System.currentTimeMillis());
	}
	
	@Test
	public void stressTree() {
		ISearchTree<String> node = new SearchTree<>();
		this.stress("tree", node, false);
	}
	
	@Test
	public void stressHeap() {
		ISearchTree<String> node = new HeapSearchTree<>();
		this.stress("heap", node, false);
	}
	
	@Test
	public void stressRadix() {
		ISearchTree<String> node = new RadixSearchTree<>();
		this.stress("radix", node, false);
	}
	
	private void stress(String loggerName, ISearchTree<String> node, boolean withWildcards) {
		Logger logger = LoggerFactory.getLogger("[" + loggerName + "]"); 
		logger.info("=================================================================================");
		logger.info("Beginning test for {}", node.getClass().getName());
		logger.info("=================================================================================");
		
		long start = System.currentTimeMillis();
		
		long startIndex = 0;
		if(withWildcards) {
			startIndex = 1;
			node.put(Strings.repeat("#", SearchTreeStressTest.LENGTH), "max-any-character");
		}
		
		long nextPercent = SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL;
		for(long i = startIndex; i < SearchTreeStressTest.SIZE; i++) {
			String generated = this.generate(withWildcards);
			node.put(generated, generated.substring(0,1));
			
			if(i > nextPercent) {
				logger.info("inserted {} (of {}) items in : {}ms", new Object[]{i-1, SearchTreeStressTest.SIZE, (System.currentTimeMillis() - start)});
				nextPercent = nextPercent + (SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL);	
			}
		}
		
		double insertOperations = SearchTreeStressTest.SIZE;
		double delta = (System.currentTimeMillis() - start);
		double opsPerSecond = this.calculateOperationsPerSecond(insertOperations, delta);
		double milliPerOps = this.calculateMillisecondsPerOperation(insertOperations, delta);
		logger.info("inserting took: {}ms ({} insertions at {} insertions per second averaging ~{}ms per insertion)", new Object[]{delta, insertOperations, opsPerSecond, milliPerOps});

		start = System.currentTimeMillis();
		
		final Set<String> findSet = new LinkedHashSet<>();
		
		nextPercent = SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL;
		for(long i = 0; i < SearchTreeStressTest.SIZE; i++) {
			String generated = this.generate(false);
			findSet.addAll(node.find(generated));
			
			if(i > nextPercent) {
				logger.info("searched {} (of {}) items in : {}ms", new Object[]{i-1, SearchTreeStressTest.SIZE, (System.currentTimeMillis() - start)});
				nextPercent = nextPercent + (SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL);	
			}
		}
		
		double compareOperations = (long)Math.pow(SearchTreeStressTest.SIZE, 2);
		delta = (System.currentTimeMillis() - start);
		opsPerSecond = this.calculateOperationsPerSecond(compareOperations, delta);
		milliPerOps = this.calculateMillisecondsPerOperation(compareOperations, delta);
		logger.info("searching took: {}ms ({} comparisons at {} comparisons per second averaging ~{}ms per operation)", new Object[]{delta, compareOperations, opsPerSecond, milliPerOps});

		logger.info("found: " + findSet.size() + " nodes");
		for(String found : findSet) {
			logger.info("\t{}", found);
		}
	}
	
	private double calculateOperationsPerSecond(double operations, double delta) {
		double deltaSeconds = (delta*1.0d) / 1000.0d;
		double operationsPerSecond = operations/deltaSeconds;
		return operationsPerSecond;
	}
	
	private double calculateMillisecondsPerOperation(double operations, double delta) {
		double millisecondsPerOperation = delta / operations;
		return millisecondsPerOperation;
	}
	
	private String generate(boolean wildcards) {
		// get base allowed string
		final String allowed;
		
		// wildcards
		if(wildcards) {
			allowed = SearchTreeStressTest.WILD_ALLOWED;
		} else {
			allowed = SearchTreeStressTest.ALLOWED;
		}
		
		final int length = SearchTreeStressTest.LENGTH;
		char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = allowed.charAt(this.random.nextInt(allowed.length()));
	    }
	    return new String(text);		
	}
	
}
