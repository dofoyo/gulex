package com.rhb.gulex.financialstatements;

import org.junit.Test;

import com.rhb.gulex.financialstatement.spider.DownloadFinancialStatements;
import com.rhb.gulex.financialstatement.spider.DownloadFinancialStatementsFromSina;
import com.rhb.gulex.stock.spider.DownloadStockList;
import com.rhb.gulex.stock.spider.DownloadStockListFromEastmoney;

public class DownloadFinancialStatementsFromSinaTest {
	DownloadFinancialStatements dfs = new DownloadFinancialStatementsFromSina();


	//@Test
	public void testDownload(){
		String period = "20161231";
		boolean overwrite = false;
		refreshStockList();
		//dfs.down(overwrite,period);
		System.out.println("testDownload over!");
	}
	
	private void refreshStockList(){
		DownloadStockList dsl = new DownloadStockListFromEastmoney();
		dsl.go();
		System.out.println("DownloadStockListFromEastmoney done!");
	}

	
}
