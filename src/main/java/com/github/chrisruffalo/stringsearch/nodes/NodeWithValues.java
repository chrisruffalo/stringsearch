package com.github.chrisruffalo.stringsearch.nodes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;


public abstract class NodeWithValues<T> extends Node<T> {

	private Set<T> values;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(RadixConfiguration configuration) {
		super.init(configuration);
		
		this.values = configuration.storageFactory().createSet();
	}
	
	protected void values(Set<T> values) {
		this.values.clear();
		if(values == null) {
			return;
		}
		this.values.addAll(values);
	}
	
	public Set<T> values() {
		return this.values;
	}
	
	@Override
	public boolean supportsValues() {
		return true;
	}
	
	/**
	 * Add a single item to storage.  Null items
	 * are not accepted.
	 * 
	 * @param item
	 */
	protected void add(T item) {
		if(item == null) {
			return;
		}
		this.add(Collections.singleton(item));
	}
	
	/**
	 * Add a collection of items to storage on
	 * this node.  Does not add null items
	 * or accept null/empty lists.
	 * 
	 * @param items
	 */
	protected void add(Collection<T> items) {
		if(items == null || items.isEmpty()) {
			return;
		}		
		
		// store items temporarily
		Set<T> tempValues = new LinkedHashSet<T>();
		
		// check items for nullity
		for(T item : items) {
			if(item != null) {
				tempValues.add(item);
			}
		}
		
		// add found values to storage on the node
		if(!tempValues.isEmpty()) {
			this.values(tempValues);
		}
	}
	
	/**
	 * Adds a an array of items to the storage
	 * on this node.  A null or empty array will
	 * be ignored.  Null items will not
	 * be added to the list.
	 * 
	 * @param items
	 */
	protected void add(T... items) {
		if(items == null || items.length < 1) {
			return;
		}
		this.add(Arrays.asList(items));
	}
	
}
