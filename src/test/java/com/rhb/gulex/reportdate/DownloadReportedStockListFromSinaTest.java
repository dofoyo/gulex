package com.rhb.gulex.reportdate;

import java.util.Map;

import org.junit.Test;

import com.rhb.gulex.reportdate.spider.DownloadReportedStockListFromSina;

public class DownloadReportedStockListFromSinaTest {

	@Test
	public void test(){
		DownloadReportedStockListFromSina down = new DownloadReportedStockListFromSina();
		Map<String,String> codes = down.go("2017");
		for(Map.Entry<String, String> entry : codes.entrySet()){
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		
	}
}
