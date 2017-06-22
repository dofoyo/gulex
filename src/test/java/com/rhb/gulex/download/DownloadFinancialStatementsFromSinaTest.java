package com.rhb.gulex.download;

import org.junit.Test;

public class DownloadFinancialStatementsFromSinaTest {
	DownloadFinancialStatements dfs = new DownloadFinancialStatementsFromSina();


	@Test
	public void testDownload(){
		String period = "20161231";
		boolean overwrite = false;
		refreshStockList();
		dfs.down(overwrite,period);
		System.out.println("testDownload over!");
	}
	
	private void refreshStockList(){
		DownloadStockList dsl = new DownloadStockListFromEastmoney();
		dsl.go();
		System.out.println("DownloadStockListFromEastmoney done!");
	}

	
}
