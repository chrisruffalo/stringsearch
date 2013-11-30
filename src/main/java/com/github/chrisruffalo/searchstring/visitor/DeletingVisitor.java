package com.github.chrisruffalo.searchstring.visitor;

import com.github.chrisruffalo.searchstring.InternalNode;

public class DeletingVisitor<D> implements Visitor<D> {

	@Override
	public void at(InternalNode<D> node, int depth, int localVisits, char[] key, int index, boolean exact) {
		
	}

	@Override
	public boolean construct() {
		return false;
	}

}
