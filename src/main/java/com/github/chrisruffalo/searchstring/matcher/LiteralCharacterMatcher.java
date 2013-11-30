package com.github.chrisruffalo.searchstring.matcher;


public class LiteralCharacterMatcher implements Matcher {

	private Character local;
	
	public LiteralCharacterMatcher(Character character) {
		this.local = character;
	}
	
	@Override
	public boolean match(Character input, boolean exact) {
		return this.local.equals(input);
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
		return false;
	}

	@Override
	public boolean optional() {
		return false;
	}
}
