package com.github.chrisruffalo.searchstring;

import java.util.Collection;
import java.util.Set;

/**
 * A search node is a single node in a tree of search items.  It may have a set of values
 * associated with itself.  When a search lands on the given node it will return those
 * values.  If no match is appropriate the node will forward the search to the next 
 * node in the chain.
 * 
 * @author Chris Ruffalo
 *
 * @param <D> the type of value to store in the nodes
 */
public interface SearchNode<D> {

	/**
	 * <p>
	 * The find operation is the most useful for external users.  It is used
	 * to &quot;search&quot; the tree for matching strings.  The find operation
	 * is <strong>not exact</strong> and uses wildcards during its search process.
	 * </p>
	 * <p>
	 * A search node may contain one or more child nodes and will forward its search
	 * to them if appropriate.  
	 * </p>
	 * 
	 * @param key the string key to search for
	 * @return the set of values found while searching this node and its children 
	 */
	Set<D> find(String key);
	
	/**
	 * <p>
	 * Performs an <strong>exact</strong> match lookup on the node (and its children).  
	 * This is <i>generally</i> less useful than the &quot;find&quot; operation for 
	 * most users because it does not take into account wildcards.
	 * </p>
	 * 
	 * @param key the string key to exactly match
	 * @return the set of values found in the exactly matching node
	 */
	Set<D> lookup(String key);
	
	/**
	 * Puts values into the current node, or its children, that correspond to the
	 * given string.  An exact lookup is performed which will construct additional
	 * nodes if required.  The values are placed into the node or its children so
	 * that a later <em>find</em> or <em>lookup</em> operation will be able to
	 * target them. 
	 * 
	 * @param key the key to use for storing the values
	 * @param value the value to store at the indicated node
	 */
	void put(String key, D value);
	
	/**
	 * A convenient way to {@link #put(String, Object)} an array of items. 
	 * 
	 * @param key the key to use for storing the values
	 * @param values an array of values to store
	 */
	void put(String key, D[] values);
	
	/**
	 * A convenient way to {@link #put(String, Object)} a collection of items.
	 * 
	 * @param key
	 * @param values
	 */
	void put(String key, Collection<D> values);
	
	/**
	 * Prints to {@link System#out} the complete structure of the current node
	 * and its children.
	 * 
	 */
	void print();

}
