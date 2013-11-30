package com.github.chrisruffalo.searchstring.matcher;

public interface Matcher {

	boolean match(Character input, boolean exact);
	
	int compare(Character input);
	
	boolean optional();
	
	Character value();
	
	boolean attracts(boolean exact);
	
}
