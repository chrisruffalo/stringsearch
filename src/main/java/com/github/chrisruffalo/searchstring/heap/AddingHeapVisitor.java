package com.github.chrisruffalo.searchstring.heap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AddingHeapVisitor<D> implements HeapVisitor<D> {

	private Map<Long, Set<D>> targetValues;
	
	private Set<D> input;
	
	public AddingHeapVisitor(D input, Map<Long,Set<D>> targetValues) {
		this(Collections.singleton(input), targetValues);
	}
	
	public AddingHeapVisitor(D[] input, Map<Long,Set<D>> targetValues) {
		this(Arrays.asList(input), targetValues);
	}
	
	public AddingHeapVisitor(Collection<D> input, Map<Long,Set<D>> targetValues) {
		// todo: throw exception if input is null or empty
		
		this.targetValues = targetValues;
		this.input = new LinkedHashSet<>();
		this.input.addAll(input);
	}
	
	@Override
	public void at(long currentLevel, long indexInLevel, int stringIndex, char[] key, boolean exact) {
		// don't add values from null/empty set
		if(this.input == null || this.input.isEmpty()) {
			return;
		}
		
		// do math to get level
		long levelIndexStartsAt = (long)Math.pow(HeapNavigator.MAX_CHILD_COUNT, currentLevel);
		
		// use parent offset to load it
		long offset = levelIndexStartsAt + indexInLevel;

		// create value set for offset/index
		Set<D> valueSet = this.targetValues.get(offset);
		if(valueSet == null) {
			valueSet = new TreeSet<>();
			this.targetValues.put(offset, valueSet);
		}
		
		// add values to value set
		for(D value : this.input) {
			if(value == null) {
				continue;
			}
			valueSet.add(value);
		}	
	}

	@Override
	public boolean construct() {
		return true;
	}
	
}
