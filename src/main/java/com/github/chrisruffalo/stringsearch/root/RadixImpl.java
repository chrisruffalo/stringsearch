package com.github.chrisruffalo.stringsearch.root;

import java.util.Map;

import com.github.chrisruffalo.stringsearch.config.DefaultAtomicRadixConfiguration;
import com.github.chrisruffalo.stringsearch.config.RadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.nodes.Node;
import com.github.chrisruffalo.stringsearch.nodes.factory.NodeFactory;


/**
 * Radix implementation that is mostly not thread-safe
 * 
 * @author cruffalo
 *
 * @param <T>
 */
public class RadixImpl<T> extends AbstractRadix<T> {

	private Map<Character, Node<T>> nodeMap;
	
	public RadixImpl() {
		this(new DefaultAtomicRadixConfiguration());
	}
	
	public RadixImpl(RadixConfigurationImpl configuration) {
		super(configuration);
		
		this.nodeMap = this.configuration().storageFactory().createMap();
	}
	
	@Override
	protected Node<T> getNode(CharSequence key, boolean create) {
		Character first = key.charAt(0);
		Node<T> node = this.nodeMap.get(first);
		if(node == null && create) {
			node = NodeFactory.create(key, this.configuration(), false, false);
			this.nodeMap.put(first, node);
		}
		return node;
	}

	public void swap(Node<T> outgoing, Node<T> incoming) {
		Character first = outgoing.content().charAt(0);
		this.nodeMap.put(first, incoming);
	}
	
}
