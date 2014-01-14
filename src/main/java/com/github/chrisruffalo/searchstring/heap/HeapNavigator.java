package com.github.chrisruffalo.searchstring.heap;

import java.util.Map;

import com.github.chrisruffalo.searchstring.config.DefaultSearchConfiguration;
import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

public abstract class HeapNavigator<D> {

	public static final int MAX_CHILD_COUNT = 3;
	
	protected enum Direction {
		
		// utility
		ROOT(0),
		
		// directional
		LOWER(0),		
		SAME(1),
		HIGHER(2),
		
		// optional
		EQUAL(0),
		SHUNT(1)
		;
		
		private int offset;
		
		private Direction(int offset) {
			this.offset = offset;
		}
		
		public int offset() {
			return this.offset;
		}
		
	}
	
	private SearchConfiguration configuration;
	
	public HeapNavigator() {
		this(new DefaultSearchConfiguration());
	}
	
	public HeapNavigator(SearchConfiguration configuration) {
		if(configuration == null || configuration.nodeFactory() == null) {
			// throw error
		}
		this.configuration = configuration;
	}
	
	public void navigate(HeapVisitor<D> visitor, Map<Integer,Character> values, String key, boolean exact) {
		// die on null values
		if(visitor == null || values == null || key == null || key.isEmpty()) {
			return;
		}		
		
		final int currentLevel = 0;
		final int indexInLevel = 0;
		
		Matcher matcher = this.loadCurrent(values, currentLevel, indexInLevel);
		
		// special case to construct root of tree on navigation start
		if(matcher == null) {
			this.construct(key.charAt(0), values, -1, 0, Direction.ROOT);			
		}
		
		// navigate onward
		this.navigate(visitor, values, currentLevel, indexInLevel, key.toCharArray(), 0, exact);
	}
	
	/**
	 * Implement navigation logic for given node
	 * 
	 * @param visitor
	 * @param values
	 * @param currentLevel
	 * @param indexInLevel
	 * @param key
	 * @param stringIndex
	 * @param exact
	 */
	protected abstract void navigate(HeapVisitor<D> visitor, Map<Integer,Character> values, long currentLevel, long indexInLevel, char[] key, int stringIndex, boolean exact);

	/**
	 * Shortcut for navigating to child
	 * 
	 * @param visitor
	 * @param values
	 * @param currentLevel
	 * @param indexInLevel
	 * @param direction
	 * @param key
	 * @param stringIndex
	 * @param exact
	 */
	protected void navigateToChild(HeapVisitor<D> visitor, Map<Integer,Character> values, long currentLevel, long indexInLevel, Direction direction, char[] key, int stringIndex, boolean exact) {
		this.navigate(visitor, values, currentLevel+1, (indexInLevel * HeapNavigator.MAX_CHILD_COUNT) + direction.offset(), key, stringIndex, exact);
	}
	
	protected Matcher loadCurrent(Map<Integer,Character> values ,long levelToLoadFrom, long localOffset) {
		// do math to get level
		long levelIndexStartsAt = (long)Math.pow(HeapNavigator.MAX_CHILD_COUNT, levelToLoadFrom);
		
		// use parent offset to load it
		long offset = levelIndexStartsAt + localOffset;
		
		// return value
		//return values.get((int)offset);
		return this.configuration.nodeFactory().getMatcher(values.get((int)offset), this.configuration);
	}
	
	protected Matcher loadChild(Map<Integer,Character> values, long levelToLoadFrom, long localOffset, Direction whichDirectionToLoad) {
		return this.loadChild(values, levelToLoadFrom, localOffset, whichDirectionToLoad.offset);
	}
	
	protected Matcher loadChild(Map<Integer,Character> values, long levelToLoadFrom, long localOffset, int childOffset) {
		// do math to get level
		long levelIndexStartsAt = (long)Math.pow(HeapNavigator.MAX_CHILD_COUNT, levelToLoadFrom);
		
		// do math to get where child partition/region starts at
		long childIndexStartsAt = HeapNavigator.MAX_CHILD_COUNT * localOffset;
		
		// get index of child
		long childIndex = levelIndexStartsAt + childIndexStartsAt + childOffset;
		
		return this.configuration.nodeFactory().getMatcher(values.get((int)childIndex), this.configuration);
	}
	
	protected Matcher construct(Character local, Map<Integer,Character> values, long parentLevel, long parentOffset, Direction whichDirectionToLoad) {

		// do math to get level
		long levelIndexStartsAt = (long)Math.pow(HeapNavigator.MAX_CHILD_COUNT, parentLevel+1);
		
		// do math to get where child partition/region starts at
		long childIndexStartsAt = HeapNavigator.MAX_CHILD_COUNT * parentOffset;
		
		// get index of child
		long childIndex = levelIndexStartsAt + childIndexStartsAt + whichDirectionToLoad.offset();

		if(values.containsKey(childIndex)) {
			// return without insert
			return null;
		}
				
		// create matcher from configuration's factory
		Matcher created = this.configuration.nodeFactory().getMatcher(local, this.configuration);
		
		// put matcher into list of values
		values.put((int)childIndex, Character.valueOf(local));
		
		// return value
		return created;
	}
	
	protected boolean attracts(Map<Integer,Character> values, long levelToLoadFrom, long localOffset, boolean exact) {
		boolean attracts = false;
		
		// load the current
		Matcher current = this.loadCurrent(values, levelToLoadFrom, localOffset);
		
		// if the item is missing bail (it obviously can't "attract")
		if(current == null) {
			return false;
		}
		
		// check exact match
		attracts = attracts || current.attracts(exact);
		
		// short-circuit
		if(attracts) {
			return attracts;
		}
		
		return attracts;
	}
	
}
