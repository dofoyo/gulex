package com.rhb.gulex.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.parse.ParseStocklist;
import com.rhb.gulex.parse.ParseStocklistFromEastmoney;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.HttpDownload;
import com.rhb.gulex.util.ParseString;

public class DownloadFinancialStatementsFromSina implements
		DownloadFinancialStatements {
	private String path = "d:\\stocks\\";
	
	private void downloadBalanceSheet(String stockid) {
		//http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/300022/ctrl/all.phtml
		String url = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
		String pathAndfileName = path + stockid + "_balancesheet.xls";
		HttpDownload.saveToFile(url, pathAndfileName);
		System.out.println(stockid + "_balancesheet.xls have downloaded!");		
	}

	private void downloadCashFlow(String stockid) {
		//http://money.finance.sina.com.cn/corp/go.php/vDOWN_CashFlow/displaytype/4/stockid/300022/ctrl/all.phtml
		String url = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_CashFlow/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
		String pathAndfileName = path + stockid + "_cashflow.xls";
		HttpDownload.saveToFile(url, pathAndfileName);
		System.out.println(stockid + "_cashflow.xls have downloaded!");		
		
	}

	private void downloadProfitStatement(String stockid) {
		//http://money.finance.sina.com.cn/corp/go.php/vDOWN_ProfitStatement/displaytype/4/stockid/300022/ctrl/all.phtml
		String url = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_ProfitStatement/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
		String pathAndfileName = path + stockid + "_profitstatement.xls";
		HttpDownload.saveToFile(url, pathAndfileName);
		System.out.println(stockid + "_profitstatement.xls have downloaded!");		
		
	}
	
	private void download(String stockid){
		downloadBalanceSheet(stockid);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		downloadCashFlow(stockid);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		downloadProfitStatement(stockid);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


	public void down(boolean overwrite,String period) {
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		Map<String,String> stocklist = psl.parse();
		Stock s = null;
		
		int i=0;
		
		for(String stockid : stocklist.keySet()){
			s = new Stock(stockid,"");
			
			if(s.exists(period)){
				if(overwrite){
					download(stockid);
				}else{
					System.out.println(stockid + " exists! do NOT download");
				}
			}else{
				download(stockid);
			}
			System.out.println(i++ + "/" + stocklist.size());
		}
		
	}

	@Override
	public String downloadBalanceSheetUrl(String stockid) {
		return "http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
	}

	@Override
	public String downloadCashFlowUrl(String stockid) {
		return "http://money.finance.sina.com.cn/corp/go.php/vDOWN_CashFlow/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
	}

	@Override
	public String downloadProfitStatementUrl(String stockid) {
		return "http://money.finance.sina.com.cn/corp/go.php/vDOWN_ProfitStatement/displaytype/4/stockid/"+stockid+"/ctrl/all.phtml";
	}

	@Override
	public void down(Map<String,String> urls) {
		for(Map.Entry<String, String> entry : urls.entrySet()){
			HttpDownload.saveToFile(entry.getValue(), path + entry.getKey());
			System.out.println(entry.getKey() + " have downloaded!");		
			
			//为避免被反扒工具禁止，需要暂停一下
			try {
				Thread.sleep(10000);  //10��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
			
		}
		
	}

}
