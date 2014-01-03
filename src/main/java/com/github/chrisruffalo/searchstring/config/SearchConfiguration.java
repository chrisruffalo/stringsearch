package com.github.chrisruffalo.searchstring.config;

import java.util.Set;

import com.github.chrisruffalo.searchstring.NodeFactory;

public interface SearchConfiguration {

	NodeFactory nodeFactory();
	
	Set<Character> wildcards();
	
	Set<Character> optional();
	
	Set<Character> any();
	
	boolean caseSensitive();
	
	SearchConfiguration copy();
	
}
