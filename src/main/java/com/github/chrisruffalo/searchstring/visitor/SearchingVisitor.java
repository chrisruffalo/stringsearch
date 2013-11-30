package com.github.chrisruffalo.searchstring.visitor;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.chrisruffalo.searchstring.InternalNode;

public class SearchingVisitor<D> implements Visitor<D> {

	private Set<D> results;
	
	public SearchingVisitor() {
		this.results = null;
	}
	
	@Override
	public void at(InternalNode<D> node, int depth, int localVisits, char[] key, int index, boolean exact) {
		Set<D> foundAtNode = node.get(depth, localVisits);
		if(foundAtNode != null && !foundAtNode.isEmpty()) {
			// lazy init of set when it has some results
			if(this.results == null) {
				this.results = new LinkedHashSet<>();
			}
			this.results.addAll(foundAtNode);
		}
	}
	
	public Set<D> found() {
		if(this.results == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(this.results);
	}

	@Override
	public boolean construct() {
		return false;
	}	
}
