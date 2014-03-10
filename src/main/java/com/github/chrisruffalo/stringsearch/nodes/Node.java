package com.github.chrisruffalo.stringsearch.nodes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.chrisruffalo.stringsearch.config.RadixConfiguration;
import com.github.chrisruffalo.stringsearch.nodes.factory.NodeFactory;
import com.github.chrisruffalo.stringsearch.shared.ISwap;
import com.github.chrisruffalo.stringsearch.util.CharSequenceUtils;

public abstract class Node<T>  implements ISwap<Node<T>>, Comparable<Node<T>> {

	/**
	 * Used to initialize a node.  In general when a node with some sort of 
	 * storage is created it should be initialized then BECAUSE it wouldn't
	 * be created with a reference for a storage object unless it needed it
	 * 
	 * @param configuration
	 */
	public void init(RadixConfiguration configuration) {
		// initialize node
	}
	
	/**
	 * Swap a new node in for and old node (that is basically
	 * the same as saying that we need to update the tree
	 * to have a new view from this node forward)
	 * 
	 */
	public void swap(Node<T> outgoing, Node<T> incoming) {
		Map<Character, Node<T>> refs = this.children();

		// swap by removing old and adding incoming
		// probably should wrap this in some sort of lock
		refs.remove(outgoing.key());
		refs.put(incoming.key(), incoming);		
	}
	
	/**
	 * Item can have children
	 * 
	 * @return
	 */
	public boolean supportsChildren() {
		return false;
	}
	
	/**
	 * Item can have values
	 * 
	 * @return
	 */
	public boolean supportsValues() {
		return false;
	}
	
	// ========================================================================================================
	// ============================================= SHARED LOGIC =============================================
	// ========================================================================================================
	
	public void content(CharSequence content) {
		// no-op
	}
	
	public abstract CharSequence content();


	protected abstract boolean matches(CharSequence key);

	
	public abstract Character key();

	
	protected void children(Map<Character, Node<T>> children) {
		// no-op
	}
	
	public Map<Character, Node<T>> children() {
		return null;
	}
	
	protected void values(Set<T> values) {
		// no-op
	}
	
	public Set<T> values() {
		return null;
	}

	/**
	 * Add a single item to storage.  Null items
	 * are not accepted.
	 * 
	 * @param item
	 */
	protected void add(T item) {
		// no-op
	}
	
	/**
	 * Add a collection of items to storage on
	 * this node.  Does not add null items
	 * or accept null/empty lists.
	 * 
	 * @param items
	 */
	protected void add(Collection<T> items) {
		// no-op
	}
	
	/**
	 * Adds a an array of items to the storage
	 * on this node.  A null or empty array will
	 * be ignored.  Null items will not
	 * be added to the list.
	 * 
	 * @param items
	 */
	protected void add(T... items) {
		// no-op
	}
	
	/**
	 * FIX ME.  I AM REALLY SLOW.
	 * 
	 * @param forKey
	 * @return
	 */
	protected Node<T> findChild(CharSequence forKey) {
		// get a reference to reduce method calls
		Map<Character, Node<T>> reference = this.children();
		
		// return answer
		if(reference == null || reference.isEmpty() || forKey == null || forKey.length() < 1) {
			return null;
		}
		
		// if found, return
		return reference.get(forKey.charAt(0));
	}
	
	/**
	 * FIX ME, I AM SUPER SLOW
	 * 
	 * @param forKey
	 * @return
	 */
	private List<Node<T>> findChildren(CharSequence forKey) {
		Map<Character, Node<T>> reference = this.children();
		
		if(reference == null || !this.supportsChildren()) {
			return Collections.emptyList();
		}
		
		List<Node<T>> found = new LinkedList<Node<T>>();
		
		for(Node<T> radix : reference.values()) {
			if(radix.matches(forKey)) {
				found.add(radix);
			}
		}
		
		return found;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Node<T> o) {
		if(this == o) {
			return 0;
		}
		
		if(o == null) {
			return 1;
		}
		
		CharSequence localContent = this.content();
		CharSequence remoteContent = o.content();
		
		if(localContent == null && remoteContent == null) {
			return 0;
		}
		
		if(remoteContent == null) {
			return 1;
		}
		
		if(localContent == null) {
			return -1;
		}
		
		return Character.compare(localContent.charAt(0), remoteContent.charAt(0));
	}
	
	
	
	// ========================================================================================================
	// ============================================= PUTING LOGIC =============================================
	// ========================================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		CharSequence content = this.content();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Node) {
			Node<?> other = (Node<?>) obj;

			if (this.content() == null) {
				if (other.content() != null) {
					return false;
				}
			} else if (!this.content().equals(other.content())) {
				return false;
			}
		} else if(obj instanceof Character) {
			Character c = (Character)obj;
			
			return c.equals(this.key());
		}
		
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(ISwap<Node<T>> parent, CharSequence key, Collection<T> items, RadixConfiguration configuration) {
		// just get this ONCE
		CharSequence localContent = this.content();
		
		// no need to build more tree, 
		// we have arrived
		if(localContent.equals(key)) {
			Node<T> reference = this;
			
			reference = NodeFactory.create(localContent, configuration, this.supportsChildren(), true);
			if(this.supportsChildren()) {
				reference.children(this.children());
			}
			reference.values(this.values());
			
			if(parent != null) {
				parent.swap(this, reference);
			}
			
			reference.add(items);
			return;
		}
		
		// get prefix
		CharSequence common = CharSequenceUtils.commonPrefix(localContent, key);
	
		// how did we even get here?
		if(common == null || common.length() < 1) {
			return;
		}
		
		// (CASE 1)
		// the common sequence is equal to the content of the
		// current node which means that you should add content
		// on in another node
		// crack
		// put("crackle")
		// crack
		//  - le
		// (it could be optimized so that crackle becomes the only
		//  content but that only works if there are no other children
		//  and no other values)
		if(common.equals(localContent)) {
			CharSequence chomped = CharSequenceUtils.chomp(common, key);
			
			// need to make an item that can support children
			Node<T> reference = this;			
			if(!this.supportsChildren()) {
				boolean needsValueRef = this.needsValueReferenceForItems(this.values());
				reference = NodeFactory.create(localContent, configuration, true, needsValueRef);
				// update values
				if(needsValueRef) {
					reference.add(this.values());
				}
				// and then update it in the parent
				if(parent != null) {
					parent.swap(this, reference);
				}
			} 

			// look for existing child
			Node<T> found = reference.findChild(chomped);
			
			// if a matching child is found then do put as usual
			if(found != null) {
				found.put(reference, chomped, items, configuration);
			} 
			// otherwise we need to make a new child and add it
			else {
				boolean needsValueRef = this.needsValueReferenceForItems(items);
				Node<T> child = NodeFactory.create(chomped, configuration, false, needsValueRef);
				if(needsValueRef) {
					child.add(items);
				}
				
				// add newly created child to the list of children
				// for this node
				reference.children().put(child.key(), child);	
			}
			
		} 
		// (CASE 2)
		// the key is the common node so we need to transform the map
		// so that the common phrase becomes the node and the current
		// content is chopped
		// seeker 
		// put("see")
		// see
		//  - ker
		else if(common.equals(key)) {
			// create new parent node for this node
			boolean needsValueRef = this.needsValueReferenceForItems(items);
			Node<T> newParent = NodeFactory.create(common, configuration, true, needsValueRef);
			if(needsValueRef) {
				newParent.add(items);
			}
			
			// local content becoming new child that will get swapped in
			boolean continuationNeedsValueRef = this.needsValueReferenceForItems(this.values());
			CharSequence continuation = CharSequenceUtils.chomp(common, localContent);
			Node<T> continuingNode = NodeFactory.create(continuation, configuration, this.supportsChildren(), continuationNeedsValueRef);
			if(this.supportsChildren()) {
				continuingNode.children(this.children());
			}
			if(continuationNeedsValueRef) {
				continuingNode.add(this.values());
			}
			
			// add this child
			newParent.children().put(continuingNode.key(), continuingNode);
			
			// swap
			if(parent != null) {
				parent.swap(this, newParent);
			}
		} 
		// (CASE 3)
		// branch in two directions because the common substring
		// is shared between two nodes which means two different
		// directions that can be taken here.
		// dog
		// put("dodge")
		// do
		//  - g
		//  - dge
		else {
			// old content becoming child
			boolean continuationNeedsValueRef = this.needsValueReferenceForItems(this.values());
			CharSequence continuation = CharSequenceUtils.chomp(common, localContent);
			Node<T> continuingNode = NodeFactory.create(continuation, configuration, this.supportsChildren(), continuationNeedsValueRef);
			if(this.supportsChildren()) {
				continuingNode.children(this.children());
			}
			if(continuationNeedsValueRef) {
				continuingNode.add(this.values());
			}
			
			// new suffix
			boolean needsValueRef = this.needsValueReferenceForItems(items);
			CharSequence newSuffix = CharSequenceUtils.chomp(common, key);
			Node<T> suffixNode = NodeFactory.create(newSuffix, configuration, false, needsValueRef);
			if(needsValueRef) {
				suffixNode.add(items);
			}
			
			// create replacement that can house children
			Node<T> replacement = NodeFactory.create(common, configuration, true, false);
			
			// add children
			replacement.children().put(continuingNode.key(), continuingNode);
			replacement.children().put(suffixNode.key(), suffixNode);
			
			// swap
			if(parent != null) {
				parent.swap(this, replacement);
			}
		}		
	}
	
	// ========================================================================================================
    // ============================================ SEARCH LOGIC  =============================================
	// ========================================================================================================
	
	/**
	 * {@inheritDoc}
	 */
	public Set<T> get(CharSequence key, RadixConfiguration configuration) {
		Set<T> result = new HashSet<T>();
		this.search(result, key, true, configuration);
		return Collections.unmodifiableSet(result);
	}
	
	/**
	 * Searches children of this node for values
	 * 
	 * @param accumulator
	 * @param key
	 * @param exact
	 */
	protected void search(Set<T> accumulator, CharSequence key, boolean exact, RadixConfiguration configuration) {
		CharSequence prefix = CharSequenceUtils.commonPrefix(this.content(), key);
		
		if(prefix == null || prefix.length() < 1) {
			return;
		}
		
		// if the key isn't long enough, leave
		if(key.length() < this.content().length()) {
			return;
		}
	
		CharSequence chomp = CharSequenceUtils.chomp(prefix, key);
		if(chomp.length() == 0) {
			// we have arrived, common prefix is same
			if(this.values() != null) {
				accumulator.addAll(this.values());
			}
		} else {
			if(exact) {
				Node<T> child = this.findChild(chomp);
				if(child != null) {
					child.search(accumulator, chomp, exact, configuration);
				}
			} else {
				// search for children and continue
				List<Node<T>> matchingChildren = this.findChildren(chomp);
				for(Node<T> child : matchingChildren) {
					child.search(accumulator, chomp, exact, configuration);
				}
			}
		}
	}
		
	// ========================================================================================================
	// =========================================== UTILITY METHODS ============================================
	// ========================================================================================================
	

	private boolean needsValueReferenceForItems(Collection<T> items) {
		return items != null && !items.isEmpty();
	}
		
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [content()=" + this.content() + "]";
	}

	public String print() {
		return this.print("");
	}
	
	protected String print(String prefix) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		builder.append(this.content());
		builder.append(" : ");
		builder.append(this.getClass().getSimpleName());
		if(this.supportsValues() && this.values() != null) {
			builder.append( " => ");
			builder.append(this.values().toString());
		}
		
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < this.content().length(); i++) {
			buffer.append(" ");
		}
		
		if(this.children() != null) {
			for(Node<T> child : this.children().values()) {
				builder.append("\n");
				builder.append(child.print(prefix + buffer.toString()));
			}
		}
		return builder.toString();
	}
}
