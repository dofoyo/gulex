package com.rhb.gulex.service;

import java.util.ArrayList;
import java.util.List;

import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.parse.ParseStocklist;
import com.rhb.gulex.parse.ParseStocklistFromEastmoney;
import com.rhb.gulex.util.ParseString;

public class StockSevice {
	
	
	public void selectGoodStock(){
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
}
