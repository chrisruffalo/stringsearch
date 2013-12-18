package com.github.chrisruffalo.searchstring.visitor;

import com.github.chrisruffalo.searchstring.InternalNode;

public interface Visitor<D> {

	void at(InternalNode<D> node, int index, char[] key, boolean exact);
	
	boolean construct();
	
}
