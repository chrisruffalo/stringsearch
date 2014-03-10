package com.github.chrisruffalo.stringsearch;

import java.util.Collection;
import java.util.Set;

public interface Radix<T> {

	void put(CharSequence key, T item);

	void put(CharSequence key, T... items);

	void put(CharSequence key, Collection<T> items);

	Set<T> get(CharSequence key);
	
	String print();

}