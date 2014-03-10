package com.github.chrisruffalo.stringsearch.root;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.root.AtomicRadixImpl;


public class AtomicRadixImplTest extends AbstracatRootTypeTest {

	@Override
	public Radix<String> create() {
		return new AtomicRadixImpl<String>();
	}

		
}
