package com.github.chrisruffalo.stringsearch.config;

public class DefaultRadixConfigurationImpl extends RadixConfigurationImpl {

	public DefaultRadixConfigurationImpl() {
		super();
		
		// default special characters
		this.addBreaker('.');
		this.addMandatory('#');
		this.addWildcard('*');
		this.addOptional('?');
	}

}
