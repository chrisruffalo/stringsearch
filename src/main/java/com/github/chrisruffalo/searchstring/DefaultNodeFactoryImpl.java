package com.github.chrisruffalo.searchstring;

import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap;

import java.util.Map;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;
import com.github.chrisruffalo.searchstring.matcher.AnyCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.LiteralCharacterMatcher;
import com.github.chrisruffalo.searchstring.matcher.Matcher;

/**
 * Constructs nodes according to the configuration settings given
 * by the {@link SearchConfiguration} as employs a cache to
 * reuse {@link Matcher} values
 * 
 * @author Chris Ruffalo
 *
 */
public class DefaultNodeFactoryImpl implements NodeFactory {
	
	private Map<Character, Matcher> matcherCache;
	
	public DefaultNodeFactoryImpl() {
		this.matcherCache = new Char2ObjectLinkedOpenHashMap<Matcher>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <D> InternalNode<D> create(final Character local, final SearchConfiguration configuration) {
		
		// get matcher from cache or construct as needed
		final Matcher matcher = this.getMatcher(local, configuration);
		
		// create appropriate node type
		final InternalNode<D> node;
		if(configuration.wildcards().contains(local)) {
			node = new OptionalNode<>(matcher, true, configuration);
		} else if(configuration.optional().contains(local)) {
			node = new OptionalNode<>(matcher, false, configuration);
		} else {
			node = new DirectionalNode<>(matcher, configuration);
		}

		return node;
	}
	
	/**
	 * Constructs a matcher (as needed) from the details in {@link SearchConfiguration} or
	 * pulls it from the cache of previous matchers
	 * 
	 * @param local
	 * @param configuration
	 * @return
	 */
	@Override
	public Matcher getMatcher(final Character local, final SearchConfiguration configuration) {

		if(local == null) {
			return null;
		}
		
		final char value = local.charValue();
		// lookup matcher from cache and return if valid
		Matcher matcher = this.matcherCache.get(value);
		if(matcher != null) {
			return matcher;
		}
		
		// get appropriate matcher for input character
		if(configuration.wildcards().contains(local)) {
			matcher = new AnyCharacterMatcher(local);
		} else if(configuration.optional().contains(local)) {
			matcher = new AnyCharacterMatcher(local);
		} else if(configuration.any().contains(local)) {
			matcher = new AnyCharacterMatcher(local);
		} else {
			matcher = new LiteralCharacterMatcher(local);
		}
		
		// put in cache
		this.matcherCache.put(value, matcher);
		
		// return constructed (now cached) matcher
		return matcher;
	}
}
