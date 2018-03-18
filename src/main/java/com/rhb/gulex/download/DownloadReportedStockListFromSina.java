package com.rhb.gulex.download;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rhb.gulex.util.HttpDownload;
import com.rhb.gulex.util.ParseString;

@Service("DownloadReportedStockListFromSina")
public class DownloadReportedStockListFromSina implements DownloadReportedStockList {

	@Override
	public String[] go(String year) {
		List<String> codes = new ArrayList<String>();
		String page = "1";
		String url = "http://finance.sina.com.cn/realstock/income_statement/"+year+"-12-31/issued_pdate_de_"+page+".html";
		String result = HttpDownload.getResult(url);
		
		codes.addAll(getCodes(result));
		
		Integer pages = getPages(result);
		for(int i=2; i<=pages; i++){
			url =  "http://finance.sina.com.cn/realstock/income_statement/"+year+"-12-31/issued_pdate_de_"+Integer.toString(i)+".html";
			result = HttpDownload.getResult(url);
			codes.addAll(getCodes(result));
		}
		
		//System.out.println(pages);
		
		return codes.toArray(new String[codes.size()]);
		
	}
	
	
	private List<String> getCodes(String str){
		String regexp = "nc.shtml\">|</a>";
		List<String> list = ParseString.subStrings(str,regexp);
		List<String> codes = new ArrayList<String>();
		for(String s : list){
			if(ParseString.isDigital(s)){
				codes.add(s);
			}
		}
		
		return codes;

	}
	
	private Integer getPages(String str){
		Integer i = 0;
		String regexp = "<a href=\'issued_pdate_de_.*?.html\'>|</a>";
		List<String> list = ParseString.subStrings(str,regexp);
		for(String s : list){
			//System.out.println(s);
			if(i < ParseString.toInteger(s)){
				i = ParseString.toInteger(s);
			}
		}
		
		return i;
	}


}
