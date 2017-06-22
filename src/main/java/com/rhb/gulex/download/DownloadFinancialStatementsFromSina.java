package com.rhb.gulex.download;

import java.util.List;

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
		downloadCashFlow(stockid);
		downloadProfitStatement(stockid);
		
	}


	public void down(boolean overwrite,String period) {
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		List<String> stocklist = psl.parse();
		String stockid = null;
		Stock s = null;
		for(int i=0; i<stocklist.size(); i++){
			String stock = (String)stocklist.get(i);
			stockid = ParseString.subString(stock,"(",")");
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
			System.out.println(i + "/" + stocklist.size());
		}
		
		
	}
	
}
