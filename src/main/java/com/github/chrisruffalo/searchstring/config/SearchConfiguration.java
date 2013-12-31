package com.github.chrisruffalo.searchstring.config;

import java.util.Set;

public interface SearchConfiguration {

	Set<Character> wildcards();
	
	Set<Character> optional();
	
	Set<Character> any();
	
	boolean caseSensitive();
	
	SearchConfiguration copy();
	
}