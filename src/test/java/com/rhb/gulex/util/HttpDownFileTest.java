package com.rhb.gulex.util;

import org.junit.Test;

public class HttpDownFileTest {

	@Test
	public void downloadBalanceSheet(){
		String stockNo = "601398";

		String destUrl = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/"+stockNo+"/ctrl/all.phtml";
		System.out.println(destUrl);
		String fileName = "C:\\" + stockNo + ".xls";
		HttpDownload.saveToFile(destUrl, fileName);
		System.out.println("test done!");

	}
	
}
