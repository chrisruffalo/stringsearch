package com.github.chrisruffalo.stringsearch.nodes.factory;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;
import com.github.chrisruffalo.stringsearch.nodes.Node;
import com.github.chrisruffalo.stringsearch.nodes.sequence.CharSequenceLeaf;
import com.github.chrisruffalo.stringsearch.nodes.sequence.CharSequenceLeafWithValues;
import com.github.chrisruffalo.stringsearch.nodes.sequence.CharSequenceNode;
import com.github.chrisruffalo.stringsearch.nodes.sequence.CharSequenceNodeWithValues;
import com.github.chrisruffalo.stringsearch.nodes.single.SingleCharLeaf;
import com.github.chrisruffalo.stringsearch.nodes.single.SingleCharLeafWithValues;
import com.github.chrisruffalo.stringsearch.nodes.single.SingleCharNode;
import com.github.chrisruffalo.stringsearch.nodes.single.SingleCharNodeWithValues;

public class NodeFactory {

	public static <T> Node<T> create(CharSequence content, RadixConfiguration configuration, boolean hasChildren, boolean hasValues) {
		
		int length = content.length();
		
		Node<T> node = null;
		
		if(hasChildren && hasValues) {
			if(1 == length) {
				node = new SingleCharNodeWithValues<T>();
			} else {
				node = new CharSequenceNodeWithValues<T>();
			}
		} else if(hasChildren) {
			if(1 == length) {
				node = new SingleCharNode<T>();
			} else {
				node = new CharSequenceNode<T>();
			}
		} else if(hasValues) {
			if(1 == length) {
				node = new SingleCharLeafWithValues<T>();
			} else {
				node = new CharSequenceLeafWithValues<T>();
			}
		} else {
			if(1 == length) {
				node = new SingleCharLeaf<T>();
			} else {
				node = new CharSequenceLeaf<T>();
			}
		}
		
		// initialize node
		node.init(configuration);
		
		// set content
		node.content(content);		
		
		return node;
		
	}
	
}
