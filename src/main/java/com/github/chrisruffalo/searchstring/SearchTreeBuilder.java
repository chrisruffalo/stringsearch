package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.DefaultSearchConfiguration;
import com.github.chrisruffalo.searchstring.config.SearchConfigurationImpl;


public class SearchTreeBuilder {

	private SearchConfigurationImpl configuration;
	
	public SearchTreeBuilder() {
		 this.configuration = new DefaultSearchConfiguration();
	}
	
	public SearchTreeBuilder addWildcardCharacter(Character wildcard) {
		this.configuration.addWildcard(wildcard);
		return this;
	}
	
	public SearchTreeBuilder addAnyMatchCharacter(Character anyMatch) {
		this.configuration.addAny(anyMatch);
		return this;
	}
	
	public SearchTreeBuilder addOptionalCharacter(Character optional) {
		this.configuration.addOptional(optional);
		return this;
	}
	
	public SearchTreeBuilder caseSensitive(boolean sensitive) {
		this.configuration.setCaseSensitive(sensitive);
		return this;
	}
	
	public SearchTreeBuilder nodeFactory(NodeFactory factory) {
		this.configuration.nodeFactory(factory);
		return this;
	}
	
	public <D> ISearchTree<D> build() {
		return new SearchTree<>(this.configuration.copy());
	}
}
