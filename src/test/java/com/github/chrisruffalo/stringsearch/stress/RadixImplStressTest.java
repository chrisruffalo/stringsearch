package com.github.chrisruffalo.stringsearch.stress;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.builder.RadixBuilder;

public class RadixImplStressTest extends RootTypeStressTest {

	@Override
	public <T> Radix<T> create() {
		return (new RadixBuilder()).build();
	}

	@Override
	public int keyLength() {
		return 53;
	}

	@Override
	public int largeSet() {
		return 1500000;
	}

	@Override
	public int smallSet() {
		return 500000;
	}

}
