package com.rhb.gulex.download;

import com.rhb.gulex.util.HttpDownload;

public class DownloadStockListFromEastmoney implements DownloadStockList {

	public void go() {
		String url = "http://quote.eastmoney.com/stocklist.html";
		String file = "d:\\stocks\\stocklist.html";
		HttpDownload.saveToFile(url, file);
		System.out.println("DownloadStockListFromEastmoney have done!");		
	}

}
