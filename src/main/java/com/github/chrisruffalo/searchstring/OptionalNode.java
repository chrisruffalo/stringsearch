package com.github.chrisruffalo.searchstring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.Matcher;
import com.github.chrisruffalo.searchstring.visitor.Visitor;

public class OptionalNode<D> extends AbstractNode<D> {

	final boolean sink;
	
	final private Matcher matcher;
	
	private InternalNode<D> next;
	
	private Logger logger;
		
	public OptionalNode(Matcher matcher, boolean sink, SearchConfiguration configuration) {
		super(configuration);
		this.matcher = matcher;
		this.sink = sink;
		this.logger = LoggerFactory.getLogger(matcher.value() + "-optional");
	}
	
	@Override
	public void visit(Visitor<D> visitor, char[] key, int index, boolean exact) {
		this.logger.trace("visiting with key {} at index {}", new String(key), index);
		 
		// if you got here at the end of a chain and the
		// match is not exact that's ok, this is optional!
		// so apply visitor!
		if(index >= key.length) {
			if(!exact && !visitor.construct()) {
				visitor.at(this, index, key, exact);
			}
			return;
		}
		
		boolean end = false;
				
		Character local = Character.valueOf(key[index]);
		boolean matches = this.matcher.match(local, exact);
		
		if(matches) {
			if(index == key.length - 1) {
				visitor.at(this, index, key, exact);
				end = true;
			}
			if(!exact && this.next != null) {
				this.next.visit(visitor, key, index, exact);
			} 
			index++;
		}
		
		if(!end && exact && visitor.construct() && this.next == null) {
			Character next = key[index];
			this.next = this.construct(next);
		}
		
		if(this.next != null) {
			this.next.visit(visitor, key, index, exact);
		}
	}

	@Override
	public boolean attracts(boolean exact) {
		return this.matcher.attracts(exact);
	}

	@Override
	public void print(String prefix, String describe, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + " " + describe + " " + this.matcher.value() + " -> " + this.contentString());
        if(this.next != null) {
        	this.next.print(prefix + (isTail ? "    " : "│   ") , "[NEXT]", true);
        }
	}
	@Override
	public boolean optional() {
		return true;
	}

}
