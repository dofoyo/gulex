package com.rhb.gulex.download;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.rhb.gulex.util.HttpDownload;
import com.rhb.gulex.util.ParseString;

@Service("DownloadStockListFromEastmoney")
public class DownloadStockListFromEastmoney implements DownloadStockList {
	
	public Map<String,String> go() {
		String url = "http://quote.eastmoney.com/stocklist.html";
		String result = HttpDownload.getResult(url);
		return parse(result);
	}
	
	private Map<String,String> parse(String source) {
		String regexp = "<a.*?>|</a>";
		
		List<String> list = ParseString.subStrings(source, regexp);
		
		String code = null;
		String prefix = null;
		Map<String,String> stocks = new HashMap<String,String>();
		for(String str : list){
			code = getCode(str);
			//System.out.println(str + " ==> " + code);
			if(code!=null && !code.isEmpty() && code.length()==6){
				prefix = code.substring(0, 2);
				if(prefix.equals("60") || prefix.equals("00") || prefix.equals("30")){
					stocks.put(code, str);
				}	
			}
		}
				
		return stocks;
	}
	
	

	private String getCode(String str){
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}


}
