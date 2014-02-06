package com.github.chrisruffalo.searchstring.heap;

import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.github.chrisruffalo.searchstring.ISearchTree;
import com.github.chrisruffalo.searchstring.NodeFactory;
import com.github.chrisruffalo.searchstring.config.DefaultSearchConfiguration;
import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

public class HeapSearchTree<D> implements ISearchTree<D> {

	private final SearchConfiguration configuration;
	
	private final Map<Long,Matcher> matcherMap;
	
	private final Map<Long,Set<D>> storedValues;
	
	private final HeapNavigator<D> navigator;
	
	/**
	 * Construct a search tree with the default configuration
	 * 
	 */
	public HeapSearchTree() {
		this(new DefaultSearchConfiguration());
	}
	
	/**
	 * Construct a search tree using a provided configuration
	 * 
	 * @param configuration
	 */
	public HeapSearchTree(SearchConfiguration configuration) {
		if(configuration == null) {
			throw new IllegalArgumentException("Provided search configuration must not be null");
		}
		this.configuration = configuration;
		
		NodeFactory factory = this.configuration.nodeFactory();
		if(factory == null) {
			throw new IllegalArgumentException("Provided node factory must not be null");
		}
		
		// construct parts
		this.matcherMap = new Long2ObjectAVLTreeMap<>();
		this.storedValues = new Long2ObjectAVLTreeMap<>();
		this.navigator = new DirectionalNavigator<>(this.configuration);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<D> find(String key) {
		return this.search(key, false);	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<D> lookup(String key) {
		return this.search(key, true);
	}

	/**
	 * Internal utility for supporting exact and inexact searches 
	 * with the same code
	 * 
	 * @param key
	 * @param exact
	 * @return
	 */
	private Set<D> search(String key, boolean exact) {
		SearchingHeapVisitor<D> searcher = new SearchingHeapVisitor<>(this.storedValues);
		this.navigator.navigate(searcher, this.matcherMap, key, exact);
		return searcher.found();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void put(String key, D value) {
		this.put(key, Collections.singleton(value));
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(String key, D[] values) {
		this.put(key, Arrays.asList(values));
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(String key, Collection<D> values) {
		if(key == null || key.isEmpty() || values == null || values.isEmpty()) {
			return;
		}
		
		// do put action
		AddingHeapVisitor<D> adder = new AddingHeapVisitor<>(values, this.storedValues);
		this.navigator.navigate(adder, this.matcherMap, key, true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void print() {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	public SearchConfiguration configuration() {
		return this.configuration;
	}

}
