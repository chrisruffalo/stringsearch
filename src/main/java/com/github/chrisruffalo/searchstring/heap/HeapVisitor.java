package com.github.chrisruffalo.searchstring.heap;


public interface HeapVisitor<D> {

	void at(long currentLevel, long indexInLevel, int stringIndex, char[] key, boolean exact);
	
	boolean construct();
	
}
