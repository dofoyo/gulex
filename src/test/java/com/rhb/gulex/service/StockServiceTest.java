package com.rhb.gulex.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Bluechip;
import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.repository.financestatement.DownloadReportedStockList;
import com.rhb.gulex.repository.traderecord.DownloadTradeRecord;
import com.rhb.gulex.service.stock.StockService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StockServiceTest {
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	DownloadReportedStockList downloadReportedStockList;   
	
	@Autowired
	DownloadTradeRecord downloadTradeData;
    
   //@Test
    public void testGetBluechips(){
/*		Map<String,Bluechip> chips= stockService.getBluechips();

    	for(Map.Entry<String, Bluechip> entry : chips.entrySet()){
    		System.out.println(entry.getValue().getCode() + ", " + entry.getValue().getName() + ", " + entry.getValue().getYearsString());
    	}
    	System.out.println("There are " + chips.size() + " bule chips");
    	

    	System.out.println("these are NO trede records.");
		String subPath = "\\trade\\dzh\\";
    	StringBuffer sb = new StringBuffer();
    	for(Map.Entry<String, Bluechip> entry : chips.entrySet()){
    		String pathAndfileName = dataPath + subPath + entry.getKey() + ".txt";
    		File file = new File(pathAndfileName);
    		if(!file.exists()){
    			if(entry.getKey().indexOf("60")==-1){
            		sb.append("SZ" + entry.getKey());
    			}else{
            		sb.append("SH" + entry.getKey());
    			}
        		sb.append(",");
    		}
    	}
    	System.out.println(sb.toString());*/
    	
    	
    }

	
    @Test
    public void testGetGoodStocks(){
    	String date = "";
    	List<StockDTO> list = stockService.getGoodStocks(LocalDate.now());
    	//List<StockDTO> list = stockService.getGoodStocks(LocalDate.parse("2018-01-01"));
    	for(StockDTO stock : list){
    		System.out.println(stock.getCode() + ", " + stock.getName() + ", " + stock.getGoodPeriod() + ", " + stock.getGoodTimes() + ", " + stock.getUpProbability());
    	}
    }
    
   
   // @Test
    public void testGetDeletedStocks(){
    	List<StockDTO> list = stockService.getDeletedStocks();
    	for(StockDTO stock : list){
    		System.out.println(stock.getCode() + "," + stock.getName() + "," + stock.getLastPeriod());
    	}    	
     }
    
    //@Test
    public void testNoReport(){
    	Integer year = 2017;
    	String[] codes = downloadReportedStockList.go(Integer.toString(year));
    	for(String code : codes){
    		System.out.println(code);
    		if(stockService.noReport(code, year)){
    			System.out.println(" NO report!");
    		}else{
    			System.out.println(" done!");
    		}
    	}
		System.out.println("**************");
    }
    
    //@Test
    public void testRefresh(){
    	String[] codes = downloadReportedStockList.go("2017");
    	stockService.setFinancialStatements(codes);
    }
    
   //@Test
    public void testGetTradeRecords(){
    	String code = "601137";
    	List<TradeRecord> trs = stockService.getTradeRecords(code);
    	for(TradeRecord tr : trs){
    		System.out.println(tr.getDate().toString() + ", " + tr.getPrice().toString() + ", " + tr.getAv120().toString()+ ", " + tr.getUpProbability().toString());
    	}
    }
    
    //@Test
    public void testGetTradeData(){
		System.out.println(LocalDateTime.now() +  "   " + Thread.currentThread().getName() + ":  下载交易数据任务开始.............");

		List<StockDTO> stocks =  stockService.getGoodStocks(LocalDate.now());
		
		int i=0;
		for(StockDTO stock : stocks){
			
			System.out.print(i++ + "/" + stocks.size() + "\r");

			downloadTradeData.go(stock.getCode());

			//完成年报下载后，刷新内存中STOCK对象
			stockService.setMarketInfo(stock.getCode());		
		}
		
		System.out.print("下载了" + stocks.size() + "只股票的交易数据");

		
		
		System.out.println(Thread.currentThread().getName() + ":  下载交易数据任务结束.............");
    }

}
