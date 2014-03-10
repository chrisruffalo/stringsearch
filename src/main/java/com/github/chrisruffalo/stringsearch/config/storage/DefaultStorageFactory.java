package com.github.chrisruffalo.stringsearch.config.storage;

import gnu.trove.decorator.TCharObjectMapDecorator;
import gnu.trove.map.hash.TCharObjectHashMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultStorageFactory implements StorageFactory {

	public <T> List<T> createList() {
		return new LinkedList<T>();
	}

	public <V> Map<Character, V> createMap() {
		return new TCharObjectMapDecorator<V>(new TCharObjectHashMap<V>(0));
	}

	public <T> Set<T> createSet() {
		return new HashSet<T>(0);
	}

}
