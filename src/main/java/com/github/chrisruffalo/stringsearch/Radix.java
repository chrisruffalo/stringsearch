package com.github.chrisruffalo.stringsearch;

import java.util.Collection;
import java.util.Set;

/**
 * Root interface for dealing with string search radix.  Provides basic operations for publishing
 * and receiving values from the tree.
 * 
 * @author cruffalo
 *
 * @param <T>
 */
public interface Radix<T> {

	/**
	 * Put a single item into the search tree
	 * 
	 * @param key
	 * @param item
	 */
	void put(CharSequence key, T item);

	/**
	 * Put an array of items into the search tree
	 * 
	 * @param key
	 * @param items
	 */
	void put(CharSequence key, T... items);

	/**
	 * Add a collection of items into the array
	 * 
	 * @param key
	 * @param items
	 */
	void put(CharSequence key, Collection<T> items);
	
	/**
	 * Get the value(s) stored at a key.  This is a <em>literal</em> search.
	 * 
	 * @param key
	 * @return
	 */
	Set<T> get(CharSequence key);
	
}