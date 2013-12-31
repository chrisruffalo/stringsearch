package com.github.chrisruffalo.searchstring;


public class Seed {

	private String key;
	
	private String[] values;
	
	public Seed(String key, String... values) {
		this.key = key;
		this.values = new String[values.length];
		System.arraycopy(values, 0, this.values, 0, values.length);
	}

	public String getKey() {
		return key;
	}

	public String[] getValues() {
		return values;
	}
}
