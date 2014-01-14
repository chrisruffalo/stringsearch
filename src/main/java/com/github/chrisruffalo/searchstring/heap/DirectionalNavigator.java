package com.github.chrisruffalo.searchstring.heap;

import java.util.Map;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

public class DirectionalNavigator<D> extends HeapNavigator<D> {

	public DirectionalNavigator() {
		super();
	}
	
	public DirectionalNavigator(SearchConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	protected void navigate(HeapVisitor<D> visitor, Map<Integer,Character> values, long currentLevel, long indexInLevel, char[] key, int stringIndex, boolean exact) {		
		// nothing to do here
		if(stringIndex >= key.length) {
			return;
		}
		
		// get the local and then perform the algorithm
		Matcher matcher = this.loadCurrent(values, currentLevel, indexInLevel);

		// get a hard character reference from the value in the string
		Character local = Character.valueOf(key[stringIndex]);
		
		// special logic for when a match happens
		if(matcher.match(local, exact)) {
			
			if(stringIndex == key.length - 1) {
				// hit local visitation
				visitor.at(currentLevel, indexInLevel, stringIndex, key, exact);
				
				// load same and do stuff with it
				Matcher same = this.loadChild(values, currentLevel+1, indexInLevel, Direction.SAME);
				if(same != null && same.attracts(exact)) {
					// navigate up to same node
					this.navigateToChild(
						 	visitor, 
						 	values, 
							currentLevel, 
							indexInLevel,
							Direction.SAME, 
							key, 
							stringIndex, 
							exact
						  );
				}
			} else {
				// check same for existence
				Matcher same = this.loadChild(values, currentLevel+1, indexInLevel, Direction.SAME);
				
				// if no matcher exists, create it
				if(visitor.construct() && same == null) {
					Character next = key[stringIndex+1];
					same = this.construct(next, values, currentLevel, indexInLevel, Direction.SAME);
				}
				
				if(same != null) {
					// navigate up to same node after consuming the character
					this.navigateToChild(
						 	visitor, 
						 	values, 
							currentLevel, 
							indexInLevel,
							Direction.SAME, 
							key, 
							stringIndex+1, 
							exact
						  );
				}
			}
		} // done with "match" section
		
		// load lower and higher points
		Matcher higher = this.loadChild(values, currentLevel+1, indexInLevel, Direction.HIGHER);
		final boolean higherAttract = this.attracts(values, currentLevel+1, indexInLevel*3+Direction.HIGHER.offset(), exact);
		
		Matcher lower = this.loadChild(values, currentLevel+1, indexInLevel, Direction.LOWER);
		final boolean lowerAttract = this.attracts(values, currentLevel+1, indexInLevel*3+Direction.LOWER.offset(), exact);
		
		// check high
		if(matcher.compare(local) < 0 || (higher != null && !exact && higherAttract)) {
			
			if(visitor.construct() && higher == null) {
				higher = this.construct(local, values, currentLevel, indexInLevel, Direction.HIGHER);
			}
			
			if(higher != null) {
				this.navigateToChild(
					 	visitor, 
					 	values, 
						currentLevel, 
						indexInLevel,
						Direction.HIGHER, 
						key, 
						stringIndex, 
						exact
					  );
			}
		}
		
		// check low
		if(matcher.compare(local) > 0 || (lower != null && !exact && lowerAttract)) {
			
			if(visitor.construct() && lower == null) {
				lower = this.construct(local, values, currentLevel, indexInLevel, Direction.LOWER);
			}
			
			if(lower != null) {
				this.navigateToChild(
					 	visitor, 
					 	values, 
						currentLevel, 
						indexInLevel,
						Direction.LOWER, 
						key, 
						stringIndex, 
						exact
					  );
			}			
		}
	}
}
;