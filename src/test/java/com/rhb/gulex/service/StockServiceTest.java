package com.rhb.gulex.service;

import java.util.Map;

import org.junit.Test;

import com.rhb.gulex.download.DownloadFinancialStatements;
import com.rhb.gulex.download.DownloadFinancialStatementsFromSina;
import com.rhb.gulex.download.DownloadStockList;
import com.rhb.gulex.download.DownloadStockListFromEastmoney;
import com.rhb.gulex.parse.ParseStocklist;
import com.rhb.gulex.parse.ParseStocklistFromEastmoney;
import com.rhb.gulex.parse.ParseStocklistFromTHS;
import com.rhb.gulex.util.FileUtil;

public class StockServiceTest {
	DownloadFinancialStatements dfs = new DownloadFinancialStatementsFromSina();
	DownloadStockList dsl = new DownloadStockListFromEastmoney();
	ParseStocklist pslEastmoney = new ParseStocklistFromEastmoney();
	ParseStocklist pslTHS = new ParseStocklistFromTHS();
    
    
    //@Test
    public void downloadFinancialStatementsFromTHS(){ //根据同花顺导出的股票清单下载财务报表
		String period = "20161231";
		boolean overwrite = false;
		
        StockService ss = new StockServiceImp(dfs,dsl,pslTHS);
        ss.downloadFinancialStatements(overwrite,period);
    	
    }

    
   // @Test
    public void downloadFinancialStatementsFromEastmoney(){//根据eastmoney的股票清单下载财务报表，已放弃
		String period = "20161231";
		boolean overwrite = false;
		
        StockService ss = new StockServiceImp(dfs,dsl,pslEastmoney);
        ss.downloadFinancialStatements(overwrite,period);
    	
    }
    
    //@Test
    public void selectGoodStocks(){
		String path = "d:\\stocks\\goodstocks.txt";
		String olds = FileUtil.readTextFile(path);
		StringBuffer sb = new StringBuffer();
		String flag;

		StockService ss = new StockServiceImp(dfs,dsl,pslTHS);
        int bYear = 2014;
        int eYear = 2016;
        Map<String,String> goods = ss.selectGoodStock(bYear, eYear);
		System.out.println("there are " + goods.size() + " good stocks");
		for(Map.Entry<String, String> entry : goods.entrySet()){
			flag = olds.contains(entry.getKey()) ? "" : "new";
			System.out.println(entry.getKey() + " " + entry.getValue() + "    " + flag);
			sb.append(entry.getKey()+entry.getValue()+",");
		}
		
		//需要确定覆盖才打开
		//FileUtil.writeTextFile(path, sb.toString(), false);

    }

}
