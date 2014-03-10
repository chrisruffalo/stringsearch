package com.github.chrisruffalo.stringsearch.config;

import com.github.chrisruffalo.stringsearch.config.storage.DefaultStorageFactory;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;

public class DefaultRadixConfiguration implements RadixConfiguration {

	public StorageFactory storageFactory() {
		return new DefaultStorageFactory();
	}

}
