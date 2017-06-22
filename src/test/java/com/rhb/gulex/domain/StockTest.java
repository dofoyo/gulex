package com.rhb.gulex.domain;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rhb.gulex.parse.ParseStocklist;
import com.rhb.gulex.parse.ParseStocklistFromEastmoney;
import com.rhb.gulex.util.ParseString;

public class StockTest {

	//@Test
	public void test(){
		int year = 2016;
		String stockid = "600030";
		String stockname = "";
		Stock stock = new Stock(stockid,stockname);
		
		System.out.println(stock.getRateOfFinancialStatements(year));
		System.out.println(stock.getDetailOfFinancialStatements(year));
		System.out.println("DAR(debt to asset ratio) = " + stock.getDAR(year));
		System.out.println("CPR(cash to profit ratio) = " + stock.getCPR(year));
		System.out.println("Rec(receivable ratio) = " + stock.getReceivableRatio(year));
		System.out.println("isOk = " + stock.isOk(year));
	}
	
	//@Test
	public void getGoodStock(){
		int bYear = 2011;
		int eYear = 2016;
		List<String> goods = new ArrayList<String>();
		
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		List<String> stocklist = psl.parse();
		String stockid = null;
		Stock stock = null;
		int i = 1;
		for(String stockname : stocklist){
			stockid = ParseString.subString(stockname,"(",")");
			System.out.println(i++ + "/" + stocklist.size());
			System.out.println("********    stockid = " + stockid);
			stock = new Stock(stockid, stockname);
			for(int year=bYear; year<=eYear; year++){
				if(stock.isOk(year)){
					goods.add(stock.getStockName() +","+ Integer.toString(year) +","+ stock.getDAR(year) +","+ stock.getCPR(year) + "," + stock.getReceivableRatio(year));
				}
			}
		}
		
		System.out.println("there are " + goods.size() + " good stocks");
		for(String s : goods){
			System.out.println(s);
		}
	}
		
	//@Test
	public void getAverageAccountsReceivableTurnoverRatio(){ //上市公司应收账款周转率
		List<String> goods = new ArrayList<String>();
		
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		List<String> stocklist = psl.parse();
		String stockid = null;
		Stock stock = null;
		int i = 1;
		int year = 2015;
		double ratio = 0.0;
		int j = 0;
		for(String stockname : stocklist){
			stockid = ParseString.subString(stockname,"(",")");
			stock = new Stock(stockid, stockname);
			Double d = stock.getTurnoverRatioOfReceivable(year);
			System.out.print(i++ + "/" + stocklist.size());
			System.out.println("," + stockname + "," + d);
			if(!d.isInfinite() && !d.isNaN()){
				ratio = ratio + d;
				j++;
				
			}
		}
		
		System.out.println("the Average AccountsReceivable Turnover Ratio " + ratio/j + "。");

	}
	
	
}
