package com.github.chrisruffalo.stringsearch.util;

/**
 * A class for dealing with CharSequences in an abstract way
 * 
 * @author cruffalo
 *
 */
public class CharSequenceUtils {
	
	/**
	 * Private constructor for utility
	 * 
	 */
	private CharSequenceUtils() {
		
	}

	public static CharSequence commonPrefix(CharSequence first, CharSequence second) {
		if(first.equals(second) || (first.length() == 0 && second.length() == 0)) {
			return first;
		}
		
		int length = (int)Math.min(first.length(), second.length());
		
		int sameUntil = -1;
		for(int index = 0; index < length; index++) {
			if(first.charAt(index) == second.charAt(index)) {
				sameUntil = index;
			} else {
				break;
			}
		}
		
		if(sameUntil < 0) {
			return "";
		}
		
		CharSequence output = first.subSequence(0, sameUntil+1);
		
		return output;
	}
	
	public static CharSequence chomp(CharSequence prefix, CharSequence full) {
		return full.subSequence(prefix.length(), full.length());
	}
	
}
