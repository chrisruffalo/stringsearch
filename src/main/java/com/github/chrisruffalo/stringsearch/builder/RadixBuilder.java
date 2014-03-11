package com.github.chrisruffalo.stringsearch.builder;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.config.DefaultRadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;
import com.github.chrisruffalo.stringsearch.config.RadixConfigurationImpl;
import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;
import com.github.chrisruffalo.stringsearch.root.AtomicRadixImpl;
import com.github.chrisruffalo.stringsearch.root.RadixImpl;

public class RadixBuilder {

	private RadixConfigurationImpl configuration;
	
	public RadixBuilder() {
		this(true);
	}
	
	public RadixBuilder(boolean useDefaults) {
		this(useDefaults ? new DefaultRadixConfigurationImpl() : new RadixConfigurationImpl());
	}
	
	public RadixBuilder(RadixConfiguration bootstrap) {
		this.configuration = new RadixConfigurationImpl(bootstrap);
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
	
	public RadixBuilder addOptional(Character character) {
		this.configuration.addOptional(character);
		return this;
	}
	
	public RadixBuilder removeOptional(Character character) {
		this.configuration.removeOptional(character);
		return this;
	}
	
	public RadixBuilder addMandatory(Character character) {
		this.configuration.addMandatory(character);
		return this;
	}
	
	public RadixBuilder removeMandatory(Character character) {
		this.configuration.removeMandatory(character);
		return this;
	}
	
	public RadixBuilder addWildcard(Character character) {
		this.configuration.addWildcard(character);
		return this;
	}
	
	public RadixBuilder removeWildcard(Character character) {
		this.configuration.removeWildcard(character);
		return this;
	}
	
	public RadixBuilder addBreaker(Character character) {
		this.configuration.addBreaker(character);
		return this;
	}
	
	public RadixBuilder removeBreaker(Character character) {
		this.configuration.removeBreaker(character);
		return this;
	}
}
