package com.github.chrisruffalo.stringsearch.nodes;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;

public abstract class NodeWithValuesAndAtomicChildren<T> extends NodeWithValues<T> {

	// copied from NodeWithAtomicChildren: sometimes I could really use multi-inheritance
	
	private Map<Character, AtomicReference<Node<T>>> children;
	
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
			this.putChild(child.key(), child);
		}
	}
	
	@Override
	public boolean supportsChildren() {
		return true;
	}	

	@Override
	public void swap(Node<T> outgoing, Node<T> incoming) {
		Character first = outgoing.content().charAt(0);
		AtomicReference<Node<T>> reference = this.children.get(first);
		if(reference != null) {
			reference.set(incoming);
		}
	}
	
	@Override
	protected Collection<Node<T>> children() {
		List<Node<T>> children = new LinkedList<Node<T>>();
		for(AtomicReference<Node<T>> child : this.children.values()) {
			children.add(child.get());
		}
		return Collections.unmodifiableCollection(children);
	}
	
	@Override
	protected void putChild(Character key, Node<T> child) {
		AtomicReference<Node<T>> reference = this.children.get(key);
		if(reference != null) {
			reference.set(child);
		} else {
			reference = new AtomicReference<Node<T>>(child);
			this.children.put(key, reference);
		}
	}
	
	@Override
	protected Node<T> getChild(Character key) {
		AtomicReference<Node<T>> reference = this.children.get(key);
		if(reference != null) {
			return reference.get();
		}
		return null;
	}
}
