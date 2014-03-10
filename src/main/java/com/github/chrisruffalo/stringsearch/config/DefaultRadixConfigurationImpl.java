package com.github.chrisruffalo.stringsearch.config;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.DefaultStorageFactory;


public class DefaultRadixConfigurationImpl extends RadixConfigurationImpl {

	public DefaultRadixConfigurationImpl() {
		this.storageFactory(new DefaultStorageFactory());
		this.concurrency(ConcurrencyType.NONE);
	}

}
