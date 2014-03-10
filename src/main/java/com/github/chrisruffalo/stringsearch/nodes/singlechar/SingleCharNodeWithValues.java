package com.github.chrisruffalo.stringsearch.nodes.singlechar;

import com.github.chrisruffalo.stringsearch.nodes.NodeWithValuesAndChildren;

public class SingleCharNodeWithValues<T> extends NodeWithValuesAndChildren<T> {

	private Character content;
	
	@Override
	public void content(CharSequence content) {
		if(content == null || content.length() < 1) {
			this.content = '\0';
		} 
		this.content = content.charAt(0);
	}
	
	@Override
	public CharSequence content() {
		return this.content.toString();
	}

}
