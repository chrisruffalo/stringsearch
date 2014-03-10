package com.github.chrisruffalo.stringsearch.builder;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.config.DefaultRadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.config.RadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;
import com.github.chrisruffalo.stringsearch.root.AtomicRadixImpl;
import com.github.chrisruffalo.stringsearch.root.RadixImpl;

public class RadixBuilder {

	private RadixConfigurationImpl configuration;
	
	public RadixBuilder() {
		this.configuration = new DefaultRadixConfigurationImpl();
	}
	
	public <T> Radix<T> build() {
		ConcurrencyType type = this.configuration.concurrency();
		if(ConcurrencyType.STRICT.equals(type)) {
			return null;
		} else if(ConcurrencyType.SWAP.equals(type)){
			return new AtomicRadixImpl<T>(this.configuration);
		} else {
			return new RadixImpl<T>(this.configuration);
		}
	}
	
	public RadixBuilder concurrency(ConcurrencyType type) {
		this.configuration.concurrency(type);
		return this;
	}
	
	public RadixBuilder storageFactory(StorageFactory factory) {
		this.configuration.storageFactory();
		return this;
	}
}
