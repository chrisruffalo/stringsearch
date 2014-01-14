package com.github.chrisruffalo.searchstring;

import org.junit.Assert;
import org.junit.Test;

import com.github.chrisruffalo.searchstring.config.SearchConfiguration;

public class SearchTreeBuilderTest {

	@Test
	public void testConfigurationCopy() {
		SearchTreeBuilder builder = new SearchTreeBuilder();
		builder.addWildcardCharacter('*')
			   .addAnyMatchCharacter('#')
			   .addOptionalCharacter('?');
		ISearchTree<String> tree = builder.build();
		
		SearchConfiguration config = tree.configuration();
		
		Assert.assertTrue(config.wildcards().contains('*'));
		Assert.assertTrue(config.any().contains('#'));
		Assert.assertTrue(config.optional().contains('?'));
		Assert.assertTrue(config.caseSensitive());
		
		// change builder
		builder.addWildcardCharacter('+');
		builder.caseSensitive(false);
		
		// assert no change in copy
		Assert.assertFalse(config.wildcards().contains('+'));
		Assert.assertTrue(config.caseSensitive());
		
	}
	
}
