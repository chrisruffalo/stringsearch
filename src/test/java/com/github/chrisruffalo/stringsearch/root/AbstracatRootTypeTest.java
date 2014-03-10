package com.github.chrisruffalo.stringsearch.root;

import org.junit.Test;

import com.github.chrisruffalo.stringsearch.BaseTest;
import com.github.chrisruffalo.stringsearch.Radix;

public abstract class AbstracatRootTypeTest extends BaseTest {

	public abstract Radix<String> create();
	
	@Test
	public void testSingleFirstCharacter() {
		
		Radix<String> radix = this.create();
		
		// repeat inserts
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		this.insertAndCheck(radix, "one", true, "1");
		
		// move on
		this.insertAndCheck(radix, "ozone", true, "layer");
		this.insertAndCheck(radix, "orc", true, "slayer");
		this.insertAndCheck(radix, "ooze", true, "tmnt");
		this.insertAndCheck(radix, "orzo", true, "rice");
	}
	
	@Test
	public void testMultiPathTree() {
		
		Radix<String> radix = this.create();
		
		// simple tree
		this.insertAndCheck(radix, "alpha", true, "omega");
		this.insertAndCheck(radix, "alpo", true, "food");
		this.insertAndCheck(radix, "block", true, "party");
		this.insertAndCheck(radix, "black", true, "top");
		this.insertAndCheck(radix, "blog", true, "junk");
		this.insertAndCheck(radix, "chomp", true, "chomp");
		
		// second insert
		this.insertAndCheck(radix, "alpha", false, "charlie");
	}
	
	@Test
	public void testSingleCharExtension() {
		
		Radix<String> radix = this.create();
		
		// simple tree
		this.insertAndCheck(radix, "a", true, "frog");
		this.insertAndCheck(radix, "again", true, "extended");
		
		// check
		this.check(radix, "a", true, "frog");
		
		// try with no content
		radix.put("b");
		
		// extends
		this.insertAndCheck(radix, "button", true, "push", "click");
	}
	
	@Test
	public void testReductionToSingleChars() {
		
		Radix<String> radix = this.create();
		
		// should have lots of single letters
		radix.put("abork");
		radix.put("apork", "food");
		radix.put("about", "nothing");
		radix.put("abort", "launch");
		radix.put("abore", "yawn");
		
		// check
		this.check(radix, "apork", true, "food");
		this.check(radix, "about", true, "nothing");
		this.check(radix, "abort", true, "launch");
		this.check(radix, "abore", true, "yawn");
	}
	
	@Test
	public void testCase1() {
		
		Radix<String> radix = this.create();
		
		radix.put("crack", "ice");
		radix.put("crackle", "snap and pop");
		
		this.check(radix, "crackle", true, "snap and pop");
	}
	
	@Test
	public void testCase2() {
		
		Radix<String> radix = this.create();
		
		radix.put("seeker", "looker");
		this.check(radix, "seeker", true, "looker");
		
		radix.put("see", "eye");
		
		this.check(radix, "seeker", true, "looker");
		this.check(radix, "see", true, "eye");
	}
	
	@Test
	public void testCase3() {

		Radix<String> radix = this.create();
		
		radix.put("dog", "cat");
		radix.put("dodge", "ball");
		
		this.check(radix, "dog", true, "cat");
		this.check(radix, "dodge", true, "ball");
		
	}
}
