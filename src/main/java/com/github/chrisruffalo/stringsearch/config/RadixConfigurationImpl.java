package com.github.chrisruffalo.stringsearch.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.DefaultStorageFactory;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;

public class RadixConfigurationImpl implements RadixConfiguration {

	private StorageFactory storageFactory;
	
	private ConcurrencyType concurrency;
	
	private Set<Character> optional;
	
	private Set<Character> mandatory;
	
	private Set<Character> wildcard;
	
	private Set<Character> breaker;
	
	public RadixConfigurationImpl() {
		// concurrency should always start at NONE
		this.concurrency = ConcurrencyType.NONE;
		
		// storage type is always default too
		this.storageFactory = new DefaultStorageFactory();
		
		// create empty default sets
		this.optional = new HashSet<Character>();
		this.mandatory = new HashSet<Character>();
		this.wildcard = new HashSet<Character>();
		this.breaker = new HashSet<Character>();
	}
	
	public RadixConfigurationImpl(RadixConfiguration bootstrap) {
		this();
		
		// get settings from bootstrap
		this.storageFactory = bootstrap.storageFactory();
		this.concurrency = bootstrap.concurrency();
		
		// add from bootstrap
		this.optional.addAll(bootstrap.optional());
		this.mandatory.addAll(bootstrap.mandatory());
		this.wildcard.addAll(bootstrap.wildcard());
		this.breaker.addAll(bootstrap.breaker());
	}
	
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

	public void addOptional(Character character) {
		this.optional.add(character);
	}
	
	public void removeOptional(Character character) {
		this.optional.remove(character);
	}
	
	@Override
	public Set<Character> optional() {
		return Collections.unmodifiableSet(this.optional);
	}
	
	public void addMandatory(Character character) {
		this.mandatory.add(character);
	}
	
	public void removeMandatory(Character character) {
		this.mandatory.remove(character);
	}

	@Override
	public Set<Character> mandatory() {
		return Collections.unmodifiableSet(this.mandatory);
	}
	
	public void addWildcard(Character character) {
		this.wildcard.add(character);
	}
	
	public void removeWildcard(Character character) {
		this.wildcard.remove(character);
	}

	@Override
	public Set<Character> wildcard() {
		return Collections.unmodifiableSet(this.wildcard);
	}
	
	public void addBreaker(Character character) {
		this.breaker.add(character);
	}
	
	public void removeBreaker(Character character) {
		this.breaker.remove(character);
	}

	@Override
	public Set<Character> breaker() {
		return Collections.unmodifiableSet(this.breaker);
	}
	
	public Set<Character> allSpecial() {
		Set<Character> specials = new HashSet<Character>();
		specials.addAll(this.optional);
		specials.addAll(this.mandatory);
		specials.addAll(this.breaker);
		specials.addAll(this.wildcard);
		return Collections.unmodifiableSet(specials);
	}
	

}
