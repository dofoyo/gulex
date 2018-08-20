package com.rhb.gulex.traderecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rhb.gulex.traderecord.repository.TradeRecordEntity;

public class TradeRecordDTO {
	Map<LocalDate,TradeRecordEntity> entities = new HashMap<LocalDate,TradeRecordEntity>();
	
	public List<TradeRecordEntity> getTradeRecordEntities(){
		List<TradeRecordEntity> list = new ArrayList<TradeRecordEntity>();
		for(Map.Entry<LocalDate,TradeRecordEntity> entry : entities.entrySet()) {
			list.add(entry.getValue());
		}
		
		Collections.sort(list,new Comparator<TradeRecordEntity>() {

			@Override
			public int compare(TradeRecordEntity o1, TradeRecordEntity o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		
		return list;
	}

	public List<TradeRecordEntity> getBuyDays(){
		List<TradeRecordEntity> list = new ArrayList<TradeRecordEntity>();
		for(Map.Entry<LocalDate,TradeRecordEntity> entry : entities.entrySet()) {
			if(entry.getValue().getBuyDay()>0 && entry.getValue().isPriceOnAv(250)) {
				list.add(entry.getValue());
			}
		}
		
		return list;
	}
	
	public Integer getAboveAv120Days(LocalDate date) {
		
		LocalDate minDate = this.getMinDate();
		date = date.minusDays(1);

		Integer aboveDays = 0;
		TradeRecordEntity entity;
		for(int i=0 ;(i<99 && (date.isAfter(minDate) || date.equals(minDate)) );) {
			entity = entities.get(date);
			if(entity != null) {
				i++;
				if(entity.isPriceOnAv(120)) {
					aboveDays ++;
				}
			}
			date = date.minusDays(1);
		}
		
		return aboveDays;
	}
	
	public Map<String,Object> getTotalOf119(LocalDate date) {
		Map<String,Object> map = new HashMap<String,Object>(); 
		
		LocalDate minDate = this.getMinDate();
		date = date.minusDays(1);

		Integer i = 0;
		BigDecimal total = new BigDecimal(0);
		TradeRecordEntity entity;
		for( ;(i<119 && (date.isAfter(minDate) || date.equals(minDate)) );) {
			entity = entities.get(date);
			if(entity!=null) {
				i++;
				total = total.add(entity.getPrice());
			}
			date = date.minusDays(1);
		}
		
		map.put("quantity", i);
		map.put("total", total);
		return map;
	}
	
	
	public BigDecimal getMidPrice(LocalDate date,BigDecimal price) {
		Set<BigDecimal> prices = new HashSet<BigDecimal>();
		prices.add(price);
		for(Map.Entry<LocalDate, TradeRecordEntity> entry : entities.entrySet()) {
			if(entry.getValue().getDate().isBefore(date)) {
				prices.add(entry.getValue().getPrice());
			}
		}
		
		List<BigDecimal> list = new ArrayList<BigDecimal>(prices);
		
		Collections.sort(list);
		
		return list.get(prices.size()/2);

	}
	
	public void add(LocalDate date, TradeRecordEntity entity){
		entities.put(date, entity);
	}
	
	public TradeRecordEntity getSimilarTradeRecordEntity(LocalDate date){
		LocalDate minDate = this.getMinDate();
		TradeRecordEntity entity = entities.get(date);
		while(entity==null){
			date = date.minusDays(1);
			entity = entities.get(date);
			if(date.isBefore(minDate)){
				System.out.println("can NOT find trade record on " + date);
				break;
			}
		}
		return entity;
	}
	
	public TradeRecordEntity getTradeRecordEntity(LocalDate date) {
		return entities.get(date);
	}
	
	public List<LocalDate> getDates(LocalDate beginDate){
		List<LocalDate> list = new ArrayList<LocalDate>();
		
		Set<LocalDate> dates = entities.keySet();
		for(LocalDate date : dates) {
			if(date.isAfter(beginDate) || date.isEqual(beginDate)) {
				list.add(date);
			}
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	
	public LocalDate getIpoDate(){
		return this.getMinDate();
	}
	
    private LocalDate getMinDate() {
        LocalDate date = null;
        
    	for(Map.Entry<LocalDate, TradeRecordEntity> entry : entities.entrySet()){
    		if(date==null){
    			date = entry.getKey();
    		}else if(date.isAfter(entry.getKey())){
    			date = entry.getKey();
    		}
    	}
        
        return date;
    }

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		List<TradeRecordEntity> list = this.getTradeRecordEntities();
		for(TradeRecordEntity entity : list) {
			sb.append(entity.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
}
