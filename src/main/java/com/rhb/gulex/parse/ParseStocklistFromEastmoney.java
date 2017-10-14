package com.rhb.gulex.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

public class ParseStocklistFromEastmoney implements ParseStocklist {

	Map<String,String> blank;
	
	public ParseStocklistFromEastmoney(){
		Map<String,String> blank = new HashMap<String,String>();
		blank.put("600001", "邯郸钢铁,已退市，并到唐钢股份");
		blank.put("600002", "齐鲁石化控股股东中国石油化工股份有限公司自2006年3月6日至4月6日向其全体流通股股东实施了全面要约收购");
		blank.put("600003", "ST东北高速，公司连续亏损三年不能扭亏也没人借壳上市，被退市了");
		blank.put("600102", "莱钢股份,已退市，并到济南钢铁");
		blank.put("600181", "S*ST云大");
		blank.put("600205", "");
		blank.put("600253", "");
		blank.put("600263", "");
		blank.put("600296", "");
		blank.put("600349", "");
	}
	
	public Map<String,String> parse() {
		String path = "d:\\stocks\\stocklist.html";
		String source = FileUtil.readTextFile(path);
		String regexp = "<a.*?>|</a>";
		
		List<String> list = ParseString.subStrings(source, regexp);
		
		Map<String,String> stocks = new HashMap<String,String>();
		for(String s : list){
			if(s.contains("(60") || s.contains("(00") || s.contains("(300")){
				stocks.put(ParseString.subString(s,"(",")"), s);
			}
		}
				
		return stocks;
	}
	
	
	private boolean isBlank(String stockid){
		return blank.containsKey(stockid); 
	}

}
