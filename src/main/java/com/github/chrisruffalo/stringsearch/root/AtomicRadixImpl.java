package com.github.chrisruffalo.stringsearch.root;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.github.chrisruffalo.stringsearch.nodes.Node;
import com.github.chrisruffalo.stringsearch.nodes.factory.NodeFactory;

/**
 * Radix implementation that uses an atomic reference map to ensure
 * that a swap is a complete change in the tree and that any get
 * or put operation "sees" (references) a consistent view of the
 * tree.
 * 
 * @author cruffalo
 *
 * @param <T>
 */
public class AtomicRadixImpl<T> extends AbstractRadix<T> {

	private Map<Character, AtomicReference<Node<T>>> nodeMap;
	
	public AtomicRadixImpl() {
		this.nodeMap = this.configuration().storageFactory().createMap();
	}
	
	@Override
	protected Node<T> getNode(CharSequence key, boolean create) {
		Character first = key.charAt(0);
		AtomicReference<Node<T>> reference = this.nodeMap.get(first);
		if(reference == null && create) {
			Node<T> node = NodeFactory.create(key, this.configuration(), false, false);
			reference = new AtomicReference<Node<T>>(node);
			this.nodeMap.put(first, reference);
		}
		return reference.get();
	}

	public void swap(Node<T> outgoing, Node<T> incoming) {
		Character first = outgoing.content().charAt(0);
		AtomicReference<Node<T>> reference = this.nodeMap.get(first);
		if(reference != null) {
			reference.set(incoming);
		}
	}

	@Override
	protected List<Node<T>> children() {
		if(this.nodeMap.isEmpty()) {
			return Collections.emptyList();
		}
		List<Node<T>> children = new LinkedList<Node<T>>();
		for(AtomicReference<Node<T>> reference : this.nodeMap.values()) {
			if(reference == null) {
				continue;
			}			
			Node<T> node = reference.get();
			if(node != null) {
				children.add(node);
			}
		}
		return Collections.unmodifiableList(children);
	}
	
}
