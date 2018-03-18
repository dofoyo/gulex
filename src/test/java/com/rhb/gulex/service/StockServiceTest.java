package com.rhb.gulex.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.download.DownloadReportedStockList;
import com.rhb.gulex.download.DownloadTradeRecord;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StockServiceTest {
	@Autowired
	StockService stockService;
	
	@Autowired
	DownloadReportedStockList downloadReportedStockList;   
	
	@Autowired
	DownloadTradeRecord downloadTradeData;
    
   
    
    @Test
    public void testGetGoodStocks(){
    	List<StockDTO> list = stockService.getGoodStocks(LocalDate.now());
    	for(StockDTO stock : list){
    		System.out.println(stock.getCode() + ", " + stock.getName() + ", " + stock.getGoodPeriod() + ", " + stock.getUpProbability());
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
    		System.out.println(tr.getDate().toString() + ", " + tr.getPrice().toString() + ", " + tr.getAvarage().toString());
    	}
    	System.out.println("up Probability: " + stockService.getUpProbability(code));
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
			stockService.refreshMarketInfo(stock.getCode());		
		}
		
		System.out.print("下载了" + stocks.size() + "只股票的交易数据");

		
		
		System.out.println(Thread.currentThread().getName() + ":  下载交易数据任务结束.............");
    }

}
