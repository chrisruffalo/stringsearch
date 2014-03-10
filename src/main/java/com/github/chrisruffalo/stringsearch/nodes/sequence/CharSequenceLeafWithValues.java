package com.github.chrisruffalo.stringsearch.nodes.sequence;

import com.github.chrisruffalo.stringsearch.nodes.NodeWithValues;

public class CharSequenceLeafWithValues<T> extends NodeWithValues<T> {

	private CharSequence content;

	@Override
	public void content(CharSequence content) {
		this.content = content;
	}

	@Override
	public CharSequence content() {
		return this.content;
	}
	
}