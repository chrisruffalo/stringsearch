package com.github.chrisruffalo.stringsearch.shared;

/**
 * Shared interface for supporting a swap operation on both
 * the children (Nodes) and roots (Radix implementations)
 * 
 * @author cruffalo
 *
 * @param <T>
 */
public interface ISwap<T> {

	/**
	 * Remove the outgoing value as a child and
	 * insert the incoming value in its place.  If
	 * the outgoing value does not exist the 
	 * incoming value will still be used
	 * 
	 * @param outgoing
	 * @param incoming
	 */
	void swap(T outgoing, T incoming);
	
}
