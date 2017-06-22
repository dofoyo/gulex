package com.rhb.gulex.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ParseStringTest {

	@Test
	public void TestSubString(){
		String expected = "日出东方(603366)";
		String source = "<li><a target='_blank' href='http://quote.eastmoney.com/sh603366.html'>日出东方(603366)</a></li>";
		String regexp = "<a.*?>|</a>";
		String actual = ParseString.subString(source, regexp);
		assertEquals(expected,actual);
		System.out.println("TestSubString done!");
	}

	@Test
	public void TestSubString2(){
		String expected = "日出东方(603366)";
		String source = "<li><a target='_blank' href='http://quote.eastmoney.com/sh603366.html'>日出东方(603366)</a></li>";
		String regexp = "<a target='_blank' href='http://quote.eastmoney.com/sh603366.html'>|</a>";
		String actual = ParseString.subString(source, regexp);
		assertEquals(expected,actual);
		System.out.println("TestSubString2 done!");
	}
	
	@Test
	public void TestSubStrings(){
		List expected = new ArrayList();
		expected.add("日出东方(603366)");
		expected.add("今世缘(603369)");
		
		String source = "<li><a target='_blank' href='http://quote.eastmoney.com/sh603366.html'>日出东方(603366)</a></li>";
		source += "<li><a target='_blank' href='http://quote.eastmoney.com/sh603369.html'>今世缘(603369)</a></li>";
		
		String regexp = "<a.*?>|</a>";
		List actual = ParseString.subStrings(source, regexp);
		assertEquals(expected,actual);
		
		System.out.println("TestSubStrings done!");
	}
	
	@Test
	public void TestSubString3(){
		String expected = "日出东方(603366)";
		String source = "<li><a target='_blank' href='http://quote.eastmoney.com/sh603366.html'>日出东方(603366)</a></li>";
		String begin = "'>";
		String end = "</a>";
		String actual = ParseString.subString(source,begin,end);
		assertEquals(expected,actual);
		System.out.println("TestSubString3 done!");
	}
	
}
