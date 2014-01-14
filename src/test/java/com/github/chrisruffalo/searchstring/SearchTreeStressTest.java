package com.github.chrisruffalo.searchstring;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chrisruffalo.searchstring.heap.HeapSearchTree;
import com.google.common.base.Strings;

public class SearchTreeStressTest {

	// in percent
	private static final int LOG_INTERVAL = 10;
	
	private static final int SIZE = 10000;
	
	private static final int LENGTH = 150;
	
	@Test
	public void stressTree() {
		ISearchTree<String> node = new SearchTree<>();
		this.stress(node, false);
	}
	
	@Test
	public void stressHeap() {
		ISearchTree<String> node = new HeapSearchTree<>();
		this.stress(node, false);
	}
	
	private void stress(ISearchTree<String> node, boolean withWildcards) {
		Logger logger = LoggerFactory.getLogger(this.getClass()); 
		logger.info("=================================================================================");
		logger.info("Beginning test for {}", node.getClass().getName());
		logger.info("=================================================================================");
		
		
		Map<String, String> seedHolder = new TreeMap<>();
		
		long start = System.currentTimeMillis();
		
		long startIndex = 0;
		if(withWildcards) {
			startIndex = 1;
			seedHolder.put(Strings.repeat("#", SearchTreeStressTest.LENGTH), "max-any-character");
		}
		
		long nextPercent = SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL;
		for(long i = startIndex; i < SearchTreeStressTest.SIZE; i++) {
			String generated = this.generate(withWildcards);
			seedHolder.put(generated, generated);
			
			if(i > nextPercent) {
				logger.info("generated {} (of {}) keys in : {}ms", new Object[]{i-1, SearchTreeStressTest.SIZE, (System.currentTimeMillis() - start)});
				nextPercent = nextPercent + (SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL);	
			}
		}
		
		long delta = (System.currentTimeMillis() - start);
		logger.info("generating keys took: {}ms", delta);
		

		
		start = System.currentTimeMillis();
		long index = 0;
		long max = seedHolder.size();
		nextPercent = max / SearchTreeStressTest.LOG_INTERVAL;
		
		for(Entry<String,String> entry : seedHolder.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			node.put(key, value);
			
			index++;
			
			if(index > nextPercent) {
				logger.info("inserted {} (of {}) items in : {}ms", new Object[]{index-1, max, (System.currentTimeMillis() - start)});
				nextPercent = nextPercent + (max / SearchTreeStressTest.LOG_INTERVAL);	
			}
		}	
		
		double insertOperations = (long)Math.pow(SearchTreeStressTest.SIZE, 2);
		delta = (System.currentTimeMillis() - start);
		double opsPerSecond = this.calculateOperationsPerSecond(insertOperations, delta);
		double milliPerOps = this.calculateMillisecondsPerOperation(insertOperations, delta);
		logger.info("inserting took: {}ms ({} insertions at {} insertions per second averaging ~{}ms per operation)", new Object[]{delta, insertOperations, opsPerSecond, milliPerOps});

		start = System.currentTimeMillis();
		
		List<String> searchTries = new LinkedList<>();
		nextPercent = SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL;
		for(long i = 0; i < SearchTreeStressTest.SIZE; i++) {
			String generated = this.generate(false);
			searchTries.add(generated);
			
			if(i > nextPercent) {
				logger.info("generated {} (of {}) search terms in : {}ms", new Object[]{i-1, SearchTreeStressTest.SIZE, (System.currentTimeMillis() - start)});
				nextPercent = nextPercent + (SearchTreeStressTest.SIZE / SearchTreeStressTest.LOG_INTERVAL);	
			}
		}
		
		delta = (System.currentTimeMillis() - start);
		logger.info("generating terms took: {}ms", delta);

		
		// print for debug of small sets, printing large sets is a bad idea
		//node.print();
		
		start = System.currentTimeMillis();
		
		Set<String> findSet = new LinkedHashSet<>();
		index = 0;
		max = searchTries.size();
		nextPercent = max/ SearchTreeStressTest.LOG_INTERVAL;
		for(String test : searchTries) {
			findSet.addAll(node.find(test));
					
			index++;
			
			if(index > nextPercent) {
				logger.info("searched {} (of {}) items in : {}ms", new Object[]{index-1, SearchTreeStressTest.SIZE, (System.currentTimeMillis() - start)});
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
		// choose random prefix
		StringBuilder builder = new StringBuilder("");
		
		// a-z, 0-9, with optional #, ?, and *
		int max = (wildcards) ? 39 : 36;
		
		for(int i = 0; i < LENGTH; i++) {
			double iRandom = Math.random();
			int iChar = (int)(iRandom * max); 
			switch(iChar) {
				case 38:
					builder.append("?");
					break;
				case 37:
					builder.append("#");
					break;
				case 36:
					builder.append("*");
					break;
				default:
					if(iChar < 26) {
						builder.append(Character.toString((char)(iChar + 'a')));
					} else {
						builder.append(Character.toString((char)(iChar + '0')));
					}					
					break;
			}
		}
		return builder.toString();
	}
	
}
