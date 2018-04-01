package com.rhb.gulex.traderecord.spider;

import com.rhb.gulex.util.HttpDownload;

public class DownloadTradeRecordFromQt implements DownloadTradeRecord {

	@Override
	public void go(String code) {
		String url = "http://qt.gtimg.cn/q=s_sz000858,s_sz300022,s_sh600111,s_sh000001";
		String result = HttpDownload.getResult(url);
		System.out.println(result);
		
		String[] results = result.split("\n");
		for(String str : results) {
			String[] ss = str.split("~");
			System.out.println(ss[2] + "," + ss[3]);
		}
		
	}

	@Override
	public void go(String[] codes) {
		// TODO Auto-generated method stub
		
	}

}
