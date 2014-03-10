package com.github.chrisruffalo.stringsearch.config.concurrency;

/**
 * 
 * 
 * @author cruffalo
 *
 */
public enum ConcurrencyType {

	/**
	 * No special concurrency, not thread safe
	 * 
	 */
	NONE,	
	
	/**
	 * Creates a consistent view by only swapping
	 * data when it is ready.  Does not enforce
	 * any locks, locking, values, or any other
	 * sort of prevention.
	 * 
	 */
	SWAP,
	
	/**
	 * Strict locking version that orders writes
	 * and reads.
	 * 
	 */
	STRICT
	;
	
}
