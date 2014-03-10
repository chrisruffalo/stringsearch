package com.github.chrisruffalo.stringsearch.config;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;

public class RadixConfigurationImpl implements RadixConfiguration {

	private StorageFactory storageFactory;
	
	private ConcurrencyType concurrency;
	
	public void storageFactory(StorageFactory storageFactory) {
		this.storageFactory = storageFactory;
	}
	
	@Override
	public StorageFactory storageFactory() {
		return this.storageFactory;
	}

	public void concurrency(ConcurrencyType type) {
		this.concurrency = type;
	}
	
	@Override
	public ConcurrencyType concurrency() {
		return this.concurrency;
	}

}
