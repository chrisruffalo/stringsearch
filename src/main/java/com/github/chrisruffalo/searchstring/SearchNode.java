package com.github.chrisruffalo.searchstring;

import java.util.Collection;
import java.util.Set;

public interface SearchNode<D> {

	Set<D> find(String key);
	
	Set<D> lookup(String key);
	
	void put(String key, D value);
	
	void put(String key, D[] values);
	
	void put(String key, Collection<D> values);
	
	void print();

}
