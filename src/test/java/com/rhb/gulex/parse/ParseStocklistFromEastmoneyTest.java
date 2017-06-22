package com.rhb.gulex.parse;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public class ParseStocklistFromEastmoneyTest {

	@Test
	public void test(){
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		List<String> sl = psl.parse();
		assertFalse(sl.isEmpty());
		System.out.println(sl);
		System.out.println("test done");
		
	}
}
