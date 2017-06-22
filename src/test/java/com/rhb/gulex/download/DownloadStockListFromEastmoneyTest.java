package com.rhb.gulex.download;


import org.junit.Test;


public class DownloadStockListFromEastmoneyTest {
	@Test
	public void test1(){
		DownloadStockList dsl = new DownloadStockListFromEastmoney();
		dsl.go();
		System.out.println("DownloadStockListFromEastmoney done!");
	}
}
