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
	
	public void visit(Visitor<D> visitor, int depth, char[] key, int index, boolean exact) {
		this.visit(visitor, depth, key, index, exact, 0);
	}
	
	private void visit(Visitor<D> visitor, int depth, char[] key, int index, boolean exact, int visits) {
		// todo: better visit guard for optional/endless wildcard
		if(visits > 1) {
			return;
		}
		
		// nothing to do here
		if(index >= key.length) {
			if(!exact && this.matcher.optional()) {
				visitor.at(this, depth, 0, key, index, exact);
			}
			return;
		}
		
		Character local = Character.valueOf(key[index]);
		if(this.matcher.match(local, exact)) {
			if(index == key.length - 1) {
				visitor.at(this, depth, 0, key, index, exact);
			} else {
				if(!exact && this.matcher.optional()) {
					this.visit(visitor, depth+1, key, index, exact, visits+1);
				}
				this.visit(visitor, depth+1, key, index+1, exact, visits+1);
			}
		}
		
		if(this.matcher.compare(local) < 0 || (this.higher != null && !exact && this.higher.attracts(exact))) {
			if(visitor.construct() && this.higher == null) {
				this.higher = this.construct(local);
			}
			
			if(this.higher != null) {
				this.higher.visit(visitor, depth, key, index, exact);
			}
		} 
		
		if((this.matcher.compare(local) > 0 || (this.lower != null && !exact && this.lower.attracts(exact)))) {
			if(visitor.construct() && this.lower == null) {
				this.lower = this.construct(local);
			}
			
			if(this.lower != null) {
				this.lower.visit(visitor, depth, key, index, exact);
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

}
