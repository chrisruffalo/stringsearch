package com.github.chrisruffalo.stringsearch.nodes.sequence;

import com.github.chrisruffalo.stringsearch.nodes.NodeWithAtomicChildren;

public class CharSequenceAtomicNode<T> extends NodeWithAtomicChildren<T> {

	private CharSequence content;

	@Override
	public void content(CharSequence content) {
		this.content = content;
	}

	@Override
	public CharSequence content() {
		return this.content;
	}
	
	@Override
	protected boolean matches(CharSequence key) {
		return this.content().charAt(0) == key.charAt(0);
	}
	
	@Override
	public Character key() {
		return this.content.charAt(0);
	}
}
