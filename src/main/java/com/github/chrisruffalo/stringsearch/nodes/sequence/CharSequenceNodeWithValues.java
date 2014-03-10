package com.github.chrisruffalo.stringsearch.nodes.sequence;

import com.github.chrisruffalo.stringsearch.nodes.NodeWithValuesAndChildren;

public class CharSequenceNodeWithValues<T> extends NodeWithValuesAndChildren<T> {

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
