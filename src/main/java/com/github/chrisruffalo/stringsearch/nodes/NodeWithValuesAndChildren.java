package com.github.chrisruffalo.stringsearch.nodes;

import java.util.Collection;
import java.util.Map;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;

public abstract class NodeWithValuesAndChildren<T> extends NodeWithValues<T> {

	// copied from NodeWithChildren: sometimes I could really use multi-inheritance
	
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
	protected void children(Collection<Node<T>> children) {
		this.children.clear();
		if(children == null) {
			return;
		}
		for(Node<T> child : children) {
			this.children.put(child.key(), child);
		}
	}
	
	@Override
	public boolean supportsChildren() {
		return true;
	}
	
	@Override
	public void swap(Node<T> outgoing, Node<T> incoming) {
		Map<Character, Node<T>> refs = this.children;

		// swap by removing old and adding incoming
		// probably should wrap this in some sort of lock
		refs.remove(outgoing.key());
		refs.put(incoming.key(), incoming);		
	}
	
	@Override
	protected Collection<Node<T>> children() {
		return this.children.values();
	}
	
	@Override
	protected void putChild(Character key, Node<T> child) {
		this.children.put(key, child);
	}
	
	@Override
	protected Node<T> getChild(Character key) {
		return this.children.get(key);
	}
}
