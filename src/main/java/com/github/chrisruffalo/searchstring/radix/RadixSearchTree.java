package com.github.chrisruffalo.searchstring.radix;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.chrisruffalo.searchstring.ISearchTree;
import com.github.chrisruffalo.searchstring.NodeFactory;
import com.github.chrisruffalo.searchstring.config.DefaultSearchConfiguration;
import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultByteArrayNodeFactory;

public class RadixSearchTree<D> implements ISearchTree<D> {

	private final RadixTree<Set<D>> radix;
	
	private final SearchConfiguration configuration;
	
	/**
	 * Construct a search tree with the default configuration
	 * 
	 */
	public RadixSearchTree() {
		this(new DefaultSearchConfiguration());
	}
	
	/**
	 * Construct a search tree using a provided configuration
	 * 
	 * @param configuration
	 */
	public RadixSearchTree(SearchConfiguration configuration) {
		if(configuration == null) {
			throw new IllegalArgumentException("Provided search configuration must not be null");
		}
		this.configuration = configuration;
		
		NodeFactory factory = this.configuration.nodeFactory();
		if(factory == null) {
			throw new IllegalArgumentException("Provided node factory must not be null");
		}
		
		// construct parts
		this.radix = new ConcurrentRadixTree<>(new DefaultByteArrayNodeFactory());
	}
	
	@Override
	public Set<D> find(String key) {
		Set<D> value = this.radix.getValueForExactKey(key);
		if(value == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(value);
	}

	@Override
	public Set<D> lookup(String key) {
		Set<D> value = this.radix.getValueForExactKey(key);
		if(value == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(value);
	}

	@Override
	public void put(String key, D value) {
		this.put(key, Collections.singleton(value));
	}

	@Override
	public void put(String key, D[] values) {
		this.put(key, Arrays.asList(values));
	}

	@Override
	public void put(String key, Collection<D> values) {
		if(key == null || key.isEmpty() || values == null || values.isEmpty()) {
			return;
		}
		
		Set<D> current = this.radix.getValueForExactKey(key);
		if(current == null) {
			current = new LinkedHashSet<>();
			this.radix.put(key, current);
		}
		
		// filter and put values
		for(D value : values) {
			if(value == null) {
				continue;
			}
			current.add(value);
		}
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchConfiguration configuration() {
		return this.configuration;
	}

	
	
}
