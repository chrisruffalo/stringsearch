package com.github.chrisruffalo.stringsearch.util;

import java.util.Random;

public class Generator {

	private static final String BASE_RANDOM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static final String RANDOM_WITH_WILDCARDS = Generator.BASE_RANDOM + "*?#";
	
	private static final Random RANDOM = new MTRandom();
	
	public static String generateKey(int length, boolean wildcards) {
		
		StringBuilder builder = new StringBuilder();
		
		String generationSpace = wildcards ? Generator.RANDOM_WITH_WILDCARDS : Generator.BASE_RANDOM;
				
		// build random sequence
		for(int index = 0; index < length; index++) {
			// randomly get a point in the generation space
			int point = Generator.RANDOM.nextInt(generationSpace.length());
			// append to string
			builder.append(generationSpace.charAt(point));
		}
		
		// return random string
		return builder.toString();
	}
	
}
