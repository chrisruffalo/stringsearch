package com.github.chrisruffalo.stringsearch.root;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.config.DefaultRadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;
import com.github.chrisruffalo.stringsearch.nodes.Node;
import com.github.chrisruffalo.stringsearch.shared.ISwap;

/**
 * Base implementation of Radix to share methods.
 * 
 * @author cruffalo
 *
 * @param <T>
 */
public abstract class AbstractRadix<T> implements Radix<T>, ISwap<Node<T>> {

	private RadixConfiguration configuration;
	
	public AbstractRadix() {
		this(new DefaultRadixConfigurationImpl());
	}
	
	public AbstractRadix(RadixConfiguration configuration) {
		this.configuration = configuration;
	}
	
	protected abstract Node<T> getNode(CharSequence key, boolean create);

	protected RadixConfiguration configuration() {
		return this.configuration;
	}
	
	public void put(CharSequence key, T item) {
		this.put(key, Collections.singleton(item));
	}

	public void put(CharSequence key, T... items) {
		this.put(key, Arrays.asList(items));
	}

	public void put(CharSequence key, Collection<T> items) {
		Node<T> node = this.getNode(key, true);
		node.put(this, key, items, this.configuration);
	}

	public Set<T> get(CharSequence key) {
		Node<T> node = this.getNode(key, false);
		if(node != null) {
			return node.get(key, this.configuration);
		} 
		return Collections.emptySet();
	}
}
