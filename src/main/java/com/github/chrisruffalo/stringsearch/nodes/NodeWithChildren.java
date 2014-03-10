package com.github.chrisruffalo.stringsearch.nodes;

import java.util.Map;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;


public abstract class NodeWithChildren<T> extends Node<T> {

	private Map<Character, Node<T>> children;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(RadixConfiguration configuration) {
		super.init(configuration);

		this.children = configuration.storageFactory().createMap();
	}
	
	@Override
	protected void children(Map<Character, Node<T>> children) {
		this.children.clear();
		if(children == null) {
			return;
		}
		this.children.putAll(children);
	}
	
	@Override
	public Map<Character, Node<T>> children() {
		return this.children;
	}
	
	@Override
	public boolean supportsChildren() {
		return true;
	}
	
}
