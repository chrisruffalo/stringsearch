package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;
import com.github.chrisruffalo.searchstring.visitor.Visitor;


class DirectionalNode<D> extends AbstractNode<D> {
	
	private Matcher matcher;
	
	private InternalNode<D> higher;

	private InternalNode<D> lower;
	
	public DirectionalNode(Matcher matcher, SearchConfiguration configuration) {
		super(configuration);
		this.matcher = matcher;
	}
	
	public void visit(Visitor<D> visitor, char[] key, int index, boolean exact) {
		this.visit(visitor, key, index, 0, exact);
	}
	
	private void visit(Visitor<D> visitor, char[] key, int index, int visits, boolean exact) {
		// nothing to do here
		if(index >= key.length) {
			if(!exact && this.matcher.optional()) {
				visitor.at(this, index-1, visits, key, exact);
			}
			return;
		}
		
		Character local = Character.valueOf(key[index]);
		if(this.matcher.match(local, exact)) {
			if(index == key.length - 1) {
				visitor.at(this, index, visits, key, exact);
			} else {
				this.visit(visitor, key, index+1, visits+1, exact);
			}
		}
		
		if(this.matcher.compare(local) < 0 || (this.higher != null && !exact && this.higher.attracts(exact))) {
			if(visitor.construct() && this.higher == null) {
				this.higher = this.construct(local);
			}
			
			if(this.higher != null) {
				this.higher.visit(visitor, key, index, exact);
			}
		} 
		
		if((this.matcher.compare(local) > 0 || (this.lower != null && !exact && this.lower.attracts(exact)))) {
			if(visitor.construct() && this.lower == null) {
				this.lower = this.construct(local);
			}
			
			if(this.lower != null) {
				this.lower.visit(visitor, key, index, exact);
			}
		}
	}
	
	@Override
	public void print(String prefix, String describe,  boolean isTail) {
		String nodeString = prefix + (isTail ? "└── " : "├── ") + " " + describe + " " + this.matcher.value();
		String content = this.contentString();
		if(content != null  && !content.isEmpty()) {
			nodeString = nodeString + " -> "  + content;
		}
        System.out.println(nodeString);
        if(this.higher != null) {
        	this.higher.print(prefix + (isTail ? "    " : "│   ") , "[HIGH]", this.lower == null);
        }
        if(this.lower != null) {
        	this.lower.print(prefix + (isTail ? "    " : "│   ") , "[LOW]", true);
        }
    }
	
	@Override
	public boolean attracts(boolean exact) {
		return this.matcher.attracts(exact);
	}
	
	Character value() {
		return this.matcher.value();
	}
	
	InternalNode<D> low() {
		return this.lower;
	}
	
	InternalNode<D> high() {
		return this.higher;
	}

	@Override
	public boolean optional() {
		return false;
	}
}
