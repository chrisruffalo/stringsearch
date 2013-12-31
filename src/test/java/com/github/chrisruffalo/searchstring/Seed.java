package com.github.chrisruffalo.searchstring;

import java.util.Arrays;


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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seed other = (Seed) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + this.key + " -> " + Arrays.toString(this.values) + "]";
	}
}
