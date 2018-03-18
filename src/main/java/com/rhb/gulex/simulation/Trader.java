package com.rhb.gulex.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Trader {
	private Map<String,TradeDetail>	details = new HashMap<String,TradeDetail>();
	private static final BigDecimal amount = new BigDecimal(50000);
	
	public void buy(String code, LocalDate date, BigDecimal price){
		TradeDetail detail = new TradeDetail();
		detail.setCode(code);
		detail.setTradeDate(date);
		detail.setPrice(price);
		detail.setQuantity(amount.divide(price).intValue());
	}
	
	public void sell(String code, LocalDate date, BigDecimal price){
		TradeDetail detail = new TradeDetail();
		detail.setCode(code);
		detail.setTradeDate(date);
		detail.setPrice(price);
		detail.setQuantity(-1 * details.get(code).getQuantity());
	}
	
	public boolean onHand(String code){
		return details.containsKey(code);
	}
	
	public Set<String> getOnHands(){
		return details.keySet();
	}
	
	
	
	
}
