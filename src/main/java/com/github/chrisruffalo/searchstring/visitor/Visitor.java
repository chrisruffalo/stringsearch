package com.github.chrisruffalo.searchstring.visitor;

import com.github.chrisruffalo.searchstring.InternalNode;

public interface Visitor<D> {

	void at(InternalNode<D> node, int depth, int localVisits, char[] key, int index, boolean exact);
	
	boolean construct();
	
}
