package com.github.chrisruffalo.stringsearch.config;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;

public interface RadixConfiguration {

	public StorageFactory storageFactory();

	public ConcurrencyType concurrency();
	
}
