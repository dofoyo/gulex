package com.rhb.gulex.parse;

import java.util.ArrayList;
import java.util.List;

import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

public class ParseStocklistFromEastmoney implements ParseStocklist {

	public java.util.List<String> parse() {
		String path = "d:\\stocks\\stocklist.html";
		String source = FileUtil.readTextFile(path);
		String regexp = "<a.*?>|</a>";
		
		List<String> list = ParseString.subStrings(source, regexp);
		
		List<String> stocklist = new ArrayList();
		for(String s : list){
			if(s.contains("(60") || s.contains("(00") || s.contains("(300")){
				stocklist.add(s);
			}
		}
				
		return stocklist;
	}

}
