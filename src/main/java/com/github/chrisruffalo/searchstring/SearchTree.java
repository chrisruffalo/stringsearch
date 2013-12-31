package com.github.chrisruffalo.searchstring;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.github.chrisruffalo.searchstring.config.DefaultSearchConfiguration;
import com.github.chrisruffalo.searchstring.config.SearchConfiguration;

/**
 * Implements the root element of a search tree and
 * allows the end-user to call into a tree of search
 * nodes without the hastle of bootstrapping. 
 * 
 * @author Chris Ruffalo
 *
 * @param <D> the type of value to store in the tree
 */
public class SearchTree<D> implements SearchNode<D> {

	private InternalNode<D> root;

	private SearchConfiguration configuration;
	
	/**
	 * Construct a search tree with the default configuration
	 * 
	 */
	public SearchTree() {
		this.configuration = new DefaultSearchConfiguration();
	}
	
	/**
	 * Construct a search tree using a provided configuration
	 * 
	 * @param configuration
	 */
	public SearchTree(SearchConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<D> lookup(String key) {
		return this.find(key, true);
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public Set<D> find(String key) {
		return this.find(key, false);
	}
	
	/**
	 * Internal method to adapt the external {@link SearchNode} api
	 * to the {@link InternalNode} api used by the root of the tree
	 * 
	 * @param key
	 * @param exact
	 * @return
	 */
	protected Set<D> find(String key, boolean exact) {
		if(this.root != null) {
			return this.root.find(key, exact);
		}
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void put(String key, D value) {
		this.put(key, Collections.singleton(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String key, D[] values) {
		if(values == null || values.length == 0) {
			return;
		}
		this.put(key, Arrays.asList(values));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String key, Collection<D> values) {
		if(values == null || values.isEmpty()) {
			return;
		}
		
		if(key == null || key.isEmpty()) {
			return;
		}
		
		if(this.root == null) {
			char[] keyArray = key.toCharArray();
			Character local = keyArray[0];
			this.root = NodeFactory.create(null, local, this.configuration);
		}
		
		this.root.put(key, values);
	}
	
	/**
	 * Get the configuration that was used to create the search tree
	 * 
	 * @return
	 */
	public SearchConfiguration configuration() {
		return this.configuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print() {
		if(this.root != null) {
			this.root.print();
		}
	}
}
