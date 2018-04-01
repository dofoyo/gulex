package com.rhb.gulex.stock;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.reportdate.spider.DownloadReportedStockList;
import com.rhb.gulex.stock.api.StockDTO;
import com.rhb.gulex.stock.service.StockService;
import com.rhb.gulex.traderecord.spider.DownloadTradeRecord;


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
    

	
    @Test
    public void testGetGoodStocks(){
    	String date = "";
    	List<StockDTO> list = stockService.getStocks();
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
    
    
 
}
