package com.rhb.gulex.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MarketInfo {
	private List<TradeRecord> tradeRecords = new ArrayList<TradeRecord>(); 
	
	/*
	 * 根据日期查找交易记录
	 * 如果某天停牌，则找停牌前一天
	 * 
	 */
	public TradeRecord findByDate(LocalDate date){
		TradeRecord tradeRecord = null;
		
		TradeRecord tradeRecord1 = null;	
		TradeRecord tradeRecord2 = null;
		
		for(TradeRecord tr : tradeRecords){
			
			tradeRecord1 = tradeRecord2;
			
			long daysDiff = ChronoUnit.DAYS.between(tr.getDate(), date);
			tradeRecord2 = tr;
			
			//System.out.println("daysDiff = " + daysDiff);
			//System.out.println(date);
			
			if(daysDiff==0){  //该股这一天有成交记录
				tradeRecord = tradeRecord2; 
				break;
			}else if(daysDiff<0){   //该股在这一天没交易记录，说明停牌了，取停牌前一天
				//System.out.println("该股在"+date.toString()+"这天没交易记录,说明停牌了，取停牌前一天的交易数据！");
				tradeRecord = tradeRecord1;
				break;
			}
		}
		
		//没找到，说明查找的时间在历史交易时间后，因此取最后一个值，比如交易记录截止到2018年3月20日，需要找的日期是2018年4月1日。
		//此问题 的产生，要么是交易记录没更新，要么是参数传来的是未来的某一天
		//"该股在"+date.toString()+"之前没交易记录,说明此时还没上市交易，即还未到IPO时间。"
		if(tradeRecord == null && tradeRecords.size()>0){
			tradeRecord = tradeRecords.get(tradeRecords.size()-1);
		}
		return tradeRecord;
	}
	
	
	public void addTradeRecord(LocalDate date,BigDecimal price,String stockName){
		//System.out.println("marketInfo.addTradeRecord.");
		if(date!=null && price!=null && price.compareTo(new BigDecimal("0.0"))!=-1){
			TradeRecord record = new TradeRecord();
			record.setDate(date);
			record.setPrice(price);
			tradeRecords.add(record);
			record.setIncrease(calIncrease(stockName,price));
			record.setAv120(calAv120());
			record.setAboveAv120Days(calAboveAv120Days());
			//System.out.println("MarketInfo.addTradeRecord success!");

		}
	}
	
	private BigDecimal calIncrease(String stockName,BigDecimal price){
		BigDecimal increase = new BigDecimal(0);
		int start = tradeRecords.size()>300 ? tradeRecords.size()-300 : 0;
		List<TradeRecord> list = tradeRecords.subList(start, tradeRecords.size());
		BigDecimal lowestPrice = price;
		for(TradeRecord tr : list){
			if(lowestPrice.compareTo(tr.getPrice()) == 1){
				lowestPrice = tr.getPrice();
			}
		}		
		
		if(lowestPrice.compareTo(BigDecimal.ZERO) == 0){
			lowestPrice = new BigDecimal(0.01);
		}
		

		
		increase = price.subtract(lowestPrice).divide(lowestPrice,2,BigDecimal.ROUND_HALF_UP);
/*		
		if(stockName.indexOf("北京银行") !=-1){
			System.out.println("price=" + price);
			System.out.println("lowestPrice=" + lowestPrice);
			System.out.println("increase=" + increase);
		}*/
		
		return increase;
	}
	
	private BigDecimal calAv120(){
		BigDecimal total = new BigDecimal(0);
		int start = tradeRecords.size()>120 ? tradeRecords.size()-120 : 0;
		List<TradeRecord> list = tradeRecords.subList(start, tradeRecords.size());
		for(TradeRecord tr : list){
			total = total.add(tr.getPrice());
		}
		
		int i = tradeRecords.size() - start;
		
		return total.divide(new BigDecimal(i),2,BigDecimal.ROUND_HALF_UP);
	}
	
	private Integer calAboveAv120Days(){
		int above = 0;
		int start = tradeRecords.size()>100 ? tradeRecords.size()-100 : 0;
		List<TradeRecord> list = tradeRecords.subList(start, tradeRecords.size());
		for(TradeRecord tr : list){
			if(tr.isPriceOnAvarage()){
				above++;
			}
		}
		
		return above;
	
	}
	
	public List<TradeRecord> getTradeRecords(){
		return tradeRecords;
	}		
	
}
