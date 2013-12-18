package com.github.chrisruffalo.searchstring.visitor;

import com.github.chrisruffalo.searchstring.InternalNode;

public class DeletingVisitor<D> implements Visitor<D> {

	@Override
	public void at(InternalNode<D> node, int index, char[] key, boolean exact) {
		
	}

	@Override
	public boolean construct() {
		return false;
	}

}
