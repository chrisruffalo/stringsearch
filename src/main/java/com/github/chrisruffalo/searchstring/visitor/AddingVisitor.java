package com.github.chrisruffalo.searchstring.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.chrisruffalo.searchstring.InternalNode;

public class AddingVisitor<D> implements Visitor<D> {

	private Set<D> values;
	
	public AddingVisitor(D value) {
		this(Collections.singleton(value));
	}
	
	public AddingVisitor(D[] values) {
		this(Arrays.asList(values));
	}
	
	public AddingVisitor(Collection<D> values) {
		this.values = new LinkedHashSet<>();
		this.values.addAll(values);
	}
	
	@Override
	public void at(InternalNode<D> node, int depth, int localVisits, char[] key, int index, boolean exact) {
		node.add(depth, localVisits, this.values);
	}
	
	@Override
	public boolean construct() {
		return true;
	}

}
