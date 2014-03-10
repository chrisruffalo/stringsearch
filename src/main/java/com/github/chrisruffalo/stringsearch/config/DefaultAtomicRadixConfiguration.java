package com.github.chrisruffalo.stringsearch.config;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;


public class DefaultAtomicRadixConfiguration extends DefaultRadixConfigurationImpl {

	public DefaultAtomicRadixConfiguration() {
		this.concurrency(ConcurrencyType.SWAP);
	}

}
