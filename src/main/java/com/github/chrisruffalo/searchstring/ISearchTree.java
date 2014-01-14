package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;

public interface ISearchTree<D> extends SearchNode<D>{
	
	/**
	 * Get the configuration that was used to create the search tree
	 * 
	 * @return
	 */
	SearchConfiguration configuration();

}