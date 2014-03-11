package com.github.chrisruffalo.stringsearch.config;

import java.util.Set;

import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;
import com.github.chrisruffalo.stringsearch.config.storage.StorageFactory;

public interface RadixConfiguration {

	public StorageFactory storageFactory();

	public ConcurrencyType concurrency();
	
	public Set<Character> optional();
	
	public Set<Character> mandatory();
	
	public Set<Character> wildcard();
	
	public Set<Character> breaker();
	
	public Set<Character> allSpecial();
}
