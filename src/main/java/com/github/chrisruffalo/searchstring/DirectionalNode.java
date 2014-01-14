package com.github.chrisruffalo.searchstring;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;
import com.github.chrisruffalo.searchstring.visitor.Visitor;

class DirectionalNode<D> extends AbstractNode<D> {
	
	private Matcher matcher;
	
	private InternalNode<D> higher;

	private InternalNode<D> same;
	
	private InternalNode<D> lower;
	
	public DirectionalNode(Matcher matcher, SearchConfiguration configuration) {
		super(configuration);
		this.matcher = matcher;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(Visitor<D> visitor, char[] key, int index, boolean exact) {
		// nothing to do here
		if(index >= key.length) {
			return;
		}
		
		Character local = Character.valueOf(key[index]);
		if(this.matcher.match(local, exact)) {
			if(index == key.length - 1) {
				visitor.at(this, index, key, exact);
				if(this.same != null && this.same.attracts(exact)) {
					this.same.visit(visitor, key, index, exact);
				}
			} else {
				if(visitor.construct() && this.same == null) {
					Character next = key[index+1];
					this.same = this.construct(next);
				}
				
				if(this.same != null) {
					this.same.visit(visitor, key, index+1, exact);
				}
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(String prefix, String describe,  boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + " " + describe + " " + this.matcher.value() + " -> " + this.contentString());
        if(this.higher != null) {
        	this.higher.print(prefix + (isTail ? "     " : "│   ") , "[HIGH]", this.lower == null && this.same == null);
        }
        
        if(this.same != null) {
        	this.same.print(prefix + (isTail ? "     " : "│   ") , "[SAME]", this.lower == null);
        }

        if(this.lower != null) {
        	this.lower.print(prefix + (isTail ? "     " : "│   ") , "[LOW]", true);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean attracts(boolean exact) {
		return this.matcher.attracts(exact)
			   || !exact && (
			          (this.higher != null && this.higher.attracts(exact))
			       || (this.lower != null && this.lower.attracts(exact))
			   )
			;
	}
	
	Character value() {
		return this.matcher.value();
	}
	
	InternalNode<D> low() {
		return this.lower;
	}
	
	InternalNode<D> same() {
		return this.same;
	}
	
	InternalNode<D> high() {
		return this.higher;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean optional() {
		return false;
	}
}
