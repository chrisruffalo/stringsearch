package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.AnyCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.LiteralCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.Matcher;


public final class NodeFactory {

	private NodeFactory() {
		// cannot construct
	}
	
	static <D> InternalNode<D> create(InternalNode<D> parent, Character local, SearchConfiguration configuration) {
		
		final InternalNode<D> node;
		if(configuration.wildcards().contains(local)) {
			Matcher matcher = new AnyCharacterMatcher(local, true);
			node = new DirectionalNode<>(matcher, configuration);
		} else if(configuration.optional().contains(local)) {
			Matcher matcher = new AnyCharacterMatcher(local, true);
			node = new DirectionalNode<>(matcher, configuration);
		} else if(configuration.any().contains(local)) {
			Matcher matcher = new AnyCharacterMatcher(local);
			node = new DirectionalNode<>(matcher, configuration);
		} else {
			Matcher matcher = new LiteralCharacterMatcher(local);
			node = new DirectionalNode<>(matcher, configuration);
		}
		
		//System.out.println(node.getClass().getName());
		return node;
	}
}
