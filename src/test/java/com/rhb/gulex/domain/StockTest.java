package com.rhb.gulex.domain;

import org.junit.Test;

public class StockTest {

	@Test
	public void test(){
		int bYear = 2014;
		int eYear = 2016;
		String stockid = "300316";
		String stockname = "";
		Stock stock = new Stock(stockid,stockname);
		stock.refresh(bYear, eYear);
		
		System.out.println(stock.getRateOfFinancialStatements(eYear));
		System.out.println(stock.getDetailOfFinancialStatements(eYear));
		System.out.println("DAR(debt to asset ratio) = " + stock.getDAR(eYear));
		System.out.println("CPR(cash to profit ratio) = " + stock.getCPR(eYear));
		System.out.println("Rec(receivable ratio) = " + stock.getReceivableRatio(eYear));
		System.out.println("isOk = " + stock.isGood(eYear));
	}


	
	
}
