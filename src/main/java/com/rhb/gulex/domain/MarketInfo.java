package com.rhb.gulex.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MarketInfo {
	private List<TradeRecord> tradeRecords = new ArrayList<TradeRecord>(); 
	
	public BigDecimal getPrice(LocalDate date){
		BigDecimal price = null;
		for(TradeRecord record: tradeRecords){
			record.getDate();
		}
		return price;
	}
	
	public void refreshAvaragePrice(){
		Integer span = 120;  //120日均线
		BigDecimal[] avarage = new BigDecimal[span];
		TradeRecord tradeRecord = null;
		Iterator<TradeRecord> iterator = tradeRecords.iterator();
		for(int i=0; iterator.hasNext(); i++) {
		    tradeRecord = iterator.next();
		    //System.out.println("add " + entry.getValue().getPrice() + " to avarage[" + i%span + "]");
		    avarage[i%span] = tradeRecord.getPrice();
		    tradeRecord.setAvarage(getAvarage(avarage));
		}
	}

	public void addTradeRecord(LocalDate date,BigDecimal price){
		//System.out.println("marketInfo.addTradeRecord.");
		if(date!=null && price!=null && price.compareTo(new BigDecimal("0.0"))!=-1){
			TradeRecord record = new TradeRecord();
			record.setDate(date);
			record.setPrice(price);
			tradeRecords.add(record);
			//System.out.println("MarketInfo.addTradeRecord success!");

		}
	}
	
	
	private BigDecimal getAvarage(BigDecimal[] avarage){
		int i=0;
		BigDecimal bd = new BigDecimal(0);
		for(BigDecimal b : avarage){
			if(b!=null){
				bd = bd.add(b);
				i++;
			}
		}
		
		return bd.divide(new BigDecimal(i),2,BigDecimal.ROUND_HALF_UP);
		
	}
	
	public List<TradeRecord> getTradeRecords(){
		return tradeRecords;
	}		
	
	/*
	 *上涨概率
	 *最近100个交易日，股价大于120日均线的天数，为上涨概率，如设为a 
	 *股价偏离120日均线的百分比，每多1%， 减一个点，即：a - ((股价-av120)/av120 * 100)
	 *
	 */
	
	public Integer getUpProbability(){
		Integer up = this.getAboveDays();
		
		//System.out.println("aboveDays = " + this.getAboveDays() + ", bias=" + this.getBias());
		
		up = up - this.getBias();
		
		return up>0 ? up : 0;
		
	}
	
	public boolean isAboveAv120(){
		return getLastRecord().isPriceOnAvarage();
	}
	
	private Integer getAboveDays(){
		if(tradeRecords==null || tradeRecords.size()<1){
			return 0;
		}
		int i=0;
		int start = tradeRecords.size()>100 ? tradeRecords.size()-100 : 0;
		
		List<TradeRecord> list = tradeRecords.subList(start, tradeRecords.size());
		
		for(TradeRecord tr : list){
			if(tr.isPriceOnAvarage()){
				i++;
			}
		}
		return i;
	}
	

	private Integer getBias(){
		if(tradeRecords==null || tradeRecords.size()<1){
			return 0;
		}		
		
		TradeRecord tr = this.getLastRecord();
		return tr.getBias();
		
	}
	
	
	public TradeRecord getLastRecord(){
		return tradeRecords.get(tradeRecords.size()-1);
	}
	
}
