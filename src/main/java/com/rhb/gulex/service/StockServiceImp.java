package com.rhb.gulex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.download.DownloadFinancialStatements;
import com.rhb.gulex.download.DownloadStockList;
import com.rhb.gulex.download.DownloadStockListFromEastmoney;
import com.rhb.gulex.parse.ParseStocklist;
import com.rhb.gulex.parse.ParseStocklistFromEastmoney;
import com.rhb.gulex.util.ParseString;

public class StockServiceImp implements StockService{
	DownloadFinancialStatements downloadFinancialStatements;
	DownloadStockList downloadStockList;
	ParseStocklist parseStocklist;
	
	public StockServiceImp(DownloadFinancialStatements dfs,DownloadStockList dsl,ParseStocklist psl){
		downloadFinancialStatements = dfs;
		downloadStockList = dsl;
		parseStocklist = psl;
	}

	public Map<String,String> selectGoodStock(int bYear, int eYear){
		Map<String,String> goods = new HashMap<String,String>();
		
		Map<String,String> stocklist = parseStocklist.parse();
		Stock stock = null;
		int i = 1;
		for(Map.Entry<String, String> entry : stocklist.entrySet()){
			System.out.println(i++ + "/" + stocklist.size());
			System.out.println("********    stockid = " + entry.getKey());
			stock = new Stock(entry.getKey(), entry.getValue());
			stock.refresh(bYear, eYear);
			if(stock.getGoodTimes()>1 || stock.isGood(eYear)){
				goods.put(stock.getStockId(), stock.getStockName());
			}
		}
		return goods;
	}
	
	public void getAverageAccountsReceivableTurnoverRatio(){ //上市公司应收账款周转率
		List<String> goods = new ArrayList<String>();
		
		Map<String,String> stocks = parseStocklist.parse();
		String stockid = null;
		Stock stock = null;
		int i = 1;
		int year = 2015;
		double ratio = 0.0;
		int j = 0;
		for(Map.Entry<String, String> entry : stocks.entrySet()){
			stockid = entry.getKey();
			stock = new Stock(stockid, entry.getValue());
			Double d = stock.getTurnoverRatioOfReceivable(year);
			System.out.print(i++ + "/" + stocks.size());
			System.out.println("," + entry.getValue() + "," + d);
			if(!d.isInfinite() && !d.isNaN()){
				ratio = ratio + d;
				j++;
				
			}
		}
		
		System.out.println("the Average AccountsReceivable Turnover Ratio " + ratio/j + "。");
	
	}
	
	@Override
	public void downloadFinancialStatements(boolean overwrite,String period) {
    	System.out.println("************* downloadFinancialStatements begin *****************");
    	
    	//下载最新的股票列表，已放弃，  如果要用DownloadStockListFromEastmoney才有意义，
    	//downloadStockList.go();
    	
    	//解析最新的股票列表
    	Map<String,String> stocks = parseStocklist.parse();
    	
    	
    	//生成财务报告的下载清单
    	Map<String,String> downloadUrls = new HashMap<String,String>();
		Stock stock;
		for(Map.Entry<String, String> entry : stocks.entrySet()){
			System.out.println(entry.getKey() + entry.getValue());
			stock = new Stock(entry.getKey(),entry.getValue());
			if(!stock.exists(period) || overwrite){
				downloadUrls.put(entry.getKey()+"_balancesheet.xls",downloadFinancialStatements.downloadBalanceSheetUrl(entry.getKey()));
				downloadUrls.put(entry.getKey()+"_cashflow.xls",downloadFinancialStatements.downloadCashFlowUrl(entry.getKey()));
				downloadUrls.put(entry.getKey()+"_profitstatement.xls",downloadFinancialStatements.downloadProfitStatementUrl(entry.getKey()));
			}
		}
		
    		
    	//开始下载
		downloadFinancialStatements.down(downloadUrls);
		
    	System.out.println("************* downloadFinancialStatements over *****************");
	}
	
	
}
