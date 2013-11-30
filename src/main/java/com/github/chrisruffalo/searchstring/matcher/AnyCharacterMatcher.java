package com.github.chrisruffalo.searchstring.matcher;


public class AnyCharacterMatcher implements Matcher {

	private Character local;
	
	private boolean optional;
	
	public AnyCharacterMatcher(Character character) {
		this(character, false);
	}
	
	public AnyCharacterMatcher(Character character, boolean optional) {
		this.local = Character.valueOf(character);
		this.optional = optional;
	}
	
	@Override
	public boolean match(Character input, boolean exact) {
		if(exact) {
			return this.local.equals(input);
		}
		return true;
	}

	@Override
	public int compare(Character input) {
		return this.local.compareTo(Character.valueOf(input));
	}

	@Override
	public Character value() {
		return this.local;
	}
	
	@Override
	public boolean attracts(boolean exact) {
		return !exact;
	}

	@Override
	public boolean optional() {
		return this.optional;
	}
}
