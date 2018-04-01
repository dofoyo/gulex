package com.rhb.gulex.traderecord.spider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.util.HttpDownload;

@Service("DownloadTradeRecordFrom163")
public class DownloadTradeRecordFrom163 implements DownloadTradeRecord {
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/163/";
	
	@Override
	public void go(String code) {
		String ocode = code;
		String marketCode = code.indexOf("60")==0 ? "0" : "1";
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate today = LocalDate.now();
		
		if(code.equals("sh000001")) {  //针对上证指数
			marketCode =  "0";
			code = "000001"; 
		}
		
		//String start = Integer.toString(year-2) + "0101";
		String start = "20060101";
		String end = today.format(df);
		
		//http://quotes.money.163.com/service/chddata.html?code=0000001&start=20160101&end=20180316&fields=TCLOSE
		String url = "http://quotes.money.163.com/service/chddata.html?code="+marketCode+code+"&start="+start+"&end="+end+"&fields=TCLOSE";

		
		//System.out.println(url);
		
		String pathAndfileName = dataPath + subPath + ocode + "_tradedata.csv";

		//System.out.println("save trade record: " + pathAndfileName);
		HttpDownload.saveToFile(url, pathAndfileName);
		
		/*
		try {
			Thread.sleep(5000);  //为避免被反扒工具禁止，需要暂停一下
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	@Override
	public void go(String[] codes) {
		// TODO Auto-generated method stub
		
	}

}
