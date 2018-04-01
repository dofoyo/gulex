package com.rhb.gulex.traderecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rhb.gulex.traderecord.repository.TradeRecordEntity;

public class TradeRecordDTO {
	Map<LocalDate,TradeRecordEntity> entities = new HashMap<LocalDate,TradeRecordEntity>();
	
	public void add(LocalDate date, TradeRecordEntity entity){
		entities.put(date, entity);
	}
	
	@Deprecated
	public BigDecimal getMidPrice(LocalDate date){
		Set<BigDecimal> prices = new HashSet<BigDecimal>();
		for(Map.Entry<LocalDate, TradeRecordEntity> entry : entities.entrySet()){
			if(date.isAfter(entry.getKey())){
				prices.add(entry.getValue().getPrice());
			}
		}
		
		List<BigDecimal> list = new ArrayList<BigDecimal>(prices);
		
		Collections.sort(list);
		
		return list.get(prices.size()/2);
	}
	
	public TradeRecordEntity getTradeRecordEntity(LocalDate date){
		int i=0;
		TradeRecordEntity entity = entities.get(date);
		while(entity==null){
			date = date.minusDays(1);
			entity = entities.get(date);
			if(i++ > 1000){
				System.out.println("can NOT find trade record on " + date);
				break;
			}
		}
		return entity;
	}
	
	public LocalDate getIpoDate(){
		return this.getMinKey(entities);
	}
	
    private LocalDate getMinKey(Map<LocalDate,TradeRecordEntity> map) {
        LocalDate date = null;
        
        if (map != null){
        	for(Map.Entry<LocalDate, TradeRecordEntity> entry : map.entrySet()){
        		if(date==null){
        			date = entry.getKey();
        		}else if(date.isAfter(entry.getKey())){
        			date = entry.getKey();
        		}
        	}
        }
        
        return date;
    }

	@Override
	public String toString() {
		return "TradeRecordDTO [entities=" + entities + "]";
	}
	
	
	
}
