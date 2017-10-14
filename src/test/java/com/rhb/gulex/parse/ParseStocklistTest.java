package com.rhb.gulex.parse;

import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ParseStocklistTest {

	//@Test
	public void testParseStocklistFromEastmoney(){
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		Map<String,String> stocks = psl.parse();
		
		for (Map.Entry<String, String> entry : stocks.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		
		assertFalse(stocks.isEmpty());
		//System.out.println(stocks);
		System.out.println("test done");
		
	}
	
	
	@Test
	public void testParseStocklistFromTSH(){
		ParseStocklist psl = new ParseStocklistFromTHS();
		Map<String,String> stocks = psl.parse();
		
		for (Map.Entry<String, String> entry : stocks.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		
		assertFalse(stocks.isEmpty());
		//System.out.println(stocks);
		System.out.println("test done");
		
	}
}
