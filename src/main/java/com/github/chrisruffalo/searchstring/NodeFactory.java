package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

/**
 * Interface that describes node creation to allow end-users to implement
 * their own node factory for their search implementations.
 * 
 * @author Chris Ruffalo
 *
 */
public interface NodeFactory {

	/**
	 * Handles node construction
	 * 
	 * @param parent the node that is the parent of the node to be constructed
	 * @param local the character that the node represents
	 * @param configuration the configuration of the search node/tree
	 * @return a newly constructed node that can be placed as a child of the parent
	 */
	<D> InternalNode<D> create(Character local, SearchConfiguration configuration);
	
	Matcher getMatcher(final Character local, final SearchConfiguration configuration);

}