package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.AnyCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.LiteralCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

/**
 * Constructs nodes according to the configuration settings given
 * by the {@link SearchConfiguration}
 * 
 * @author Chris Ruffalo
 *
 */
public final class NodeFactory {

	private NodeFactory() {
		// cannot construct
	}

	/**
	 * Handles node construction
	 * 
	 * @param parent the node that is the parent of the node to be constructed
	 * @param local the character that the node represents
	 * @param configuration the configuration of the search node/tree
	 * @return a newly constructed node that can be placed as a child of the parent
	 */
	static <D> InternalNode<D> create(InternalNode<D> parent, Character local, SearchConfiguration configuration) {
		
		final InternalNode<D> node;
		if(configuration.wildcards().contains(local)) {
			Matcher matcher = new AnyCharacterMatcher(local);
			node = new OptionalNode<>(matcher, true, configuration);
		} else if(configuration.optional().contains(local)) {
			Matcher matcher = new AnyCharacterMatcher(local);
			node = new OptionalNode<>(matcher, false, configuration);
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
