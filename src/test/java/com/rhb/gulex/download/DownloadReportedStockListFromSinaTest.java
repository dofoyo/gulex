package com.rhb.gulex.download;

import org.junit.Test;

import com.rhb.gulex.repository.financestatement.DownloadReportedStockListFromSina;

public class DownloadReportedStockListFromSinaTest {

	@Test
	public void test(){
		DownloadReportedStockListFromSina down = new DownloadReportedStockListFromSina();
		String[] codes = down.go("2017");
		for(String code : codes){
			System.out.println(code);
		}
		System.out.println("there are " + codes.length + ". ");
		
	}
}
