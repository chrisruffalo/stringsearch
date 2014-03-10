package com.github.chrisruffalo.stringsearch.stress;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.builder.RadixBuilder;
import com.github.chrisruffalo.stringsearch.config.concurrency.ConcurrencyType;

public class AtomicRadixImplStressTest extends RootTypeStressTest {

	@Override
	public <T> Radix<T> create() {
		return (new RadixBuilder()).concurrency(ConcurrencyType.SWAP).build();
	}

	@Override
	public int keyLength() {
		return 53;
	}

	@Override
	public int largeSet() {
		return 750000;
	}

	@Override
	public int smallSet() {
		return 350000;
	}

}
