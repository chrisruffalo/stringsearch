package com.github.chrisruffalo.stringsearch.root;

import com.github.chrisruffalo.stringsearch.Radix;
import com.github.chrisruffalo.stringsearch.root.RadixImpl;


public class RadixImplTest extends AbstracatRootTypeTest {

	@Override
	public Radix<String> create() {
		return new RadixImpl<String>();
	}

		
}
