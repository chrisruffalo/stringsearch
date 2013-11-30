package com.github.chrisruffalo.searchstring;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.visitor.AddingVisitor;
import com.github.chrisruffalo.searchstring.visitor.SearchingVisitor;

public abstract class AbstractNode<D> implements InternalNode<D> {

	// depth/length, local visit count
	private Map<Integer, Map<Integer, Set<D>>> values;
	
	private SearchConfiguration configuration;
	
	public AbstractNode(SearchConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Set<D> find(String key, boolean exact) {
		SearchingVisitor<D> visitor = new SearchingVisitor<>();
		this.visit(visitor, key.toCharArray(), 0, exact);
		Set<D> results = visitor.found();
		return results;
	}

	@Override
	public void put(String key, D value) {
		this.put(key, Collections.singleton(value));
	}

	@Override
	public void put(String key, D[] values) {
		if(values == null || values.length == 0) {
			return;
		}
		this.put(key, Arrays.asList(values));
	}

	@Override
	public void put(String key, Collection<D> values) {
		if(values == null || values.isEmpty()) {
			return;
		}
		
		if(key == null || key.isEmpty()) {
			return;
		}
		
		AddingVisitor<D> addingVisitor = new AddingVisitor<>(values);
		this.visit(addingVisitor, key.toCharArray(), 0, true);
	}
	
	@Override
	public Set<D> get(int depth, int visits) {
		if(this.values == null || this.values.isEmpty() || !this.values.containsKey(depth)) {
			return Collections.emptySet();
		}
		Map<Integer, Set<D>> depthMap = this.values.get(depth);
		if(depthMap == null || depthMap.isEmpty() || !depthMap.containsKey(visits)) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(depthMap.get(visits));
	}
	
	@Override
	public void add(int depth, int visits, Collection<D> values) {
		if(values == null || values.isEmpty()) {
			return;
		}
		if(this.values == null) {
			this.values = new TreeMap<>();
		}
		Map<Integer, Set<D>> depthMap = this.values.get(depth);
		if(depthMap == null) {
			depthMap = new TreeMap<>();
			this.values.put(depth, depthMap);
		}
		Set<D> localValues = depthMap.get(visits);
		if(localValues == null) {
			localValues = new TreeSet<>();
			depthMap.put(visits, localValues);
		}
		for(D value : values) {
			if(value != null) {
				localValues.add(value);
			}
		}
	}
	
	protected String contentString() {
		if(this.values == null || this.values.isEmpty()) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
	
		// outside loop
		boolean outsideFirst = true;
		for(Entry<Integer, Map<Integer, Set<D>>> outerEntry : this.values.entrySet()) {
			Integer outerKey = outerEntry.getKey();
			Map<Integer, Set<D>> innerMap = outerEntry.getValue();
			
			if(innerMap.isEmpty()) {
				continue;
			}
			
			if(!outsideFirst) {
				builder.append(", ");
			}			
			outsideFirst = false;
			
			// outside key
			builder.append(outerKey);
			builder.append(":( ");
			
			// inside map loop
			boolean innerFirst = true;
			for(Entry<Integer, Set<D>> innerEntry : innerMap.entrySet()) {
				Integer innerKey = innerEntry.getKey();
				Set<D> values = innerEntry.getValue();
				
				if(values.isEmpty()) {
					continue;
				}
				
				if(!innerFirst) {
					builder.append(", ");
				}
				innerFirst = false;
				
				builder.append(innerKey);
				builder.append(":{");
				
				boolean valueFirst = true;
				for(D value : values) {
					
					if(value == null) {
						continue;
					}
					
					if(!valueFirst) {
						builder.append(", ");
					}
					valueFirst = false;
					
					// append actual value
					builder.append(String.valueOf(value));
					
				}
				
				builder.append("}");
			}
			
			// end outside key
			builder.append(" )");
		}
				
		return builder.toString();
	}
	
	protected InternalNode<D> construct(Character local) {
		return NodeFactory.create(this, local, this.configuration);
	}
	
	public void print() {
        print("", "", true);
    }

}
