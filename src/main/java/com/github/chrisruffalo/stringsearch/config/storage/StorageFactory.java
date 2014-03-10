package com.github.chrisruffalo.stringsearch.config.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides an interface that allows the storage implementation for
 * the backing storage to be customized at run time
 * 
 * @author cruffalo
 *
 */
public interface StorageFactory {

	<T> List<T> createList();
	
	<T> Set<T> createSet();
	
	<V> Map<Character, V> createMap();
	
}
