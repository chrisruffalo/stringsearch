package com.github.chrisruffalo.searchstring.heap;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SearchingHeapVisitor<D> implements HeapVisitor<D> {

	private Map<Long, Set<D>> targetValues;
	
	private Set<D> output;
	
	public SearchingHeapVisitor(Map<Long,Set<D>> targetValues) {
		this.targetValues = targetValues;
		this.output = new LinkedHashSet<>();
	}
	
	@Override
	public void at(long currentLevel, long indexInLevel, int stringIndex, char[] key, boolean exact) {
		// do math to get level
		long levelIndexStartsAt = (long)Math.pow(HeapNavigator.MAX_CHILD_COUNT, currentLevel);
		
		// use parent offset to load it
		long offset = levelIndexStartsAt + indexInLevel;

		// put values into offset if it exists
		Set<D> valueSet = this.targetValues.get(offset);
		if(valueSet != null && !valueSet.isEmpty()) {
			this.output.addAll(valueSet);
		}
	}
	

	public Set<D> found() {
		return Collections.unmodifiableSet(this.output);
	}


	@Override
	public boolean construct() {
		return false;
	}
	
}
