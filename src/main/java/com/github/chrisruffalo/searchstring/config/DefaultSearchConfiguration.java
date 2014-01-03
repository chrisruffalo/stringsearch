package com.github.chrisruffalo.searchstring.config;

import com.github.chrisruffalo.searchstring.DefaultNodeFactoryImpl;

public class DefaultSearchConfiguration extends SearchConfigurationImpl {

	public DefaultSearchConfiguration() {
		super();
		
		this.addAny('#');
		this.addOptional('?');
		this.addWildcard('*');
		
		this.setCaseSensitive(true);
		
		this.nodeFactory(new DefaultNodeFactoryImpl());
	}
	
}
