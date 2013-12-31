package com.github.chrisruffalo.searchstring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import com.google.common.collect.Collections2;

public abstract class AbstractSearchTreeTestCase {
	
	public Collection<List<Seed>> permutate(Seed... seeds) {
		if(seeds == null || seeds.length < 1) {
			return Collections.emptyList();
		}
		
		List<Seed> initialSeeds = Arrays.asList(seeds);
		return Collections2.permutations(initialSeeds);
	}

	public void seed(SearchNode<String> tree, List<Seed> seeds) {
		for(Seed seed : seeds) {
			if(seed.getKey() == null || seed.getValues() == null || seed.getValues().length == 0) {
				continue;
			}
			//LoggerFactory.getLogger("[seeder]").info("pushing key:{} with values {}", seed.getKey(), seed.getValues());
			tree.put(seed.getKey(), seed.getValues());
		}
	}
	
	public void check(SearchNode<String> tree, int expected, String key, boolean exact, String... match) {
		// get results from tree based on exact and api
		final Set<String> actualResults;
		if(exact) {
			actualResults = tree.lookup(key);
		} else {
			actualResults = tree.find(key);
		}
		
		// sort results for presentation and compare
		List<String> sortedResults = new ArrayList<String>(actualResults);
		Collections.sort(sortedResults);
		
		// get expected matchs if any exist
		final List<String> expectedResults;
		if(match != null && match.length > 0) { 
			expectedResults = Arrays.asList(match);
			Collections.sort(expectedResults);
		} else {
			expectedResults = Collections.emptyList();
		}
		
		// compute error string
		String error = this.buildNoMatchString(key, sortedResults, expectedResults);

		// do check and show error if a problem happens
		try {
			this.doCheck(expected, key, exact, sortedResults, expectedResults);
		} catch (AssertionError ae) {
			System.out.println(error);
			tree.print();
			// re-throw error because we just want status
			// on it and not to stop it.
			throw ae;
		}
	}
	
	public void doCheck(int expected, String key, boolean exact, List<String> sortedResults, List<String> expectedResults) {
		Assert.assertEquals(expected, sortedResults.size());
		if(expectedResults != null && !expectedResults.isEmpty()) {
			Assert.assertEquals(expectedResults, sortedResults);
		}
	}

	private String buildNoMatchString(String key, List<String> results, List<String> expectedResults) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("found: {");
		builder.append(this.arrayToStringList(results));
		builder.append("} for key '");
		builder.append(key);
		builder.append("'");
		
		if(expectedResults != null && !expectedResults.isEmpty()) {
			builder.append(" but expected {");
			builder.append(this.arrayToStringList(expectedResults));
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private String arrayToStringList(List<String> inputs) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String input : inputs) {
			if(!first) {
				builder.append(", ");
			}
			first = false;
			builder.append(input);
		}
		return builder.toString();
	}
	
}
