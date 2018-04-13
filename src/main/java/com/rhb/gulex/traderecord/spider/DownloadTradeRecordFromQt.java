package com.rhb.gulex.traderecord.spider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rhb.gulex.util.HttpDownload;


@Service("DownloadTradeRecordFromQt")
public class DownloadTradeRecordFromQt implements DownloadTradeRecord {

	@Override
	public Map<String,String> go(String code) {
		Map<String,String> map = new HashMap<String,String>();
		
		String marketCode = code.indexOf("60")==0 ? "sh" : "sz";
		if(code.equals("sh000001")) {
			marketCode="";
		}

		
		String url = "http://qt.gtimg.cn/q=" + marketCode + code;
		String result = HttpDownload.getResult(url);
		
		//System.out.println(result);
		
		String[] ss = result.split("~");
		//System.out.println(ss[2] + "," + ss[3] + "," + ss[30]);
		
		map.put("code", ss[2]);
		map.put("price", ss[3]);
		map.put("date", ss[30].substring(0, 8));
		
		return map;
		
	}



}
