package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Trader {
	private List<TradeDetail> details = new LinkedList<TradeDetail>();
	private Map<String,TradeDetail> onHands = new HashMap<String,TradeDetail>();
	private List<Account> accounts = new LinkedList<Account>();

	private  BigDecimal amount; //每次买入金额
	private  BigDecimal cash;  //现金
	private boolean overdraft;
	
	public void setOverdraft(boolean flag){
		this.overdraft = flag;
	}
	
	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void dayReport(LocalDate date){
		BigDecimal value = new BigDecimal(0);  
		
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()){
			value = value.add(entry.getValue().getPrice().multiply(new BigDecimal(entry.getValue().getQuantity())));
		}
		
		Account account = new Account();
		account.setDate(date);
		account.setCash(cash);
		account.setValue(value);
		accounts.add(account);
		
		//System.out.println(date.toString() + " " + " cash = " + cash.toString() + " value = " + account.getValue().toString());

	}
	
	public List<Account> getAccounts(){
		return this.accounts;
	}
	
	public void buy(String code, LocalDate date, BigDecimal price,String name){
		BigDecimal a = cash.compareTo(amount)==1 ? amount : cash; //不能透支 
		if(overdraft){
			a = amount; //允许透支
		}
		
		if(a.compareTo(new BigDecimal(0))==1){
			Integer quantity = a.divide(price,2,BigDecimal.ROUND_HALF_UP).intValue() /100 * 100;
			if(quantity > 0){
				TradeDetail detail = new TradeDetail();
				detail.setCode(code);
				detail.setName(name);
				detail.setTradeDate(date);
				detail.setCost(price);
				detail.setPrice(price);
				detail.setQuantity(quantity); 
				details.add(detail);
				
				cash = cash.subtract(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量为负，所以用 substract
				
				onHands.put(code,detail);			
				
			}
		}
	}
	
	public void sell(String code, LocalDate date, BigDecimal price,String name){
		TradeDetail detail = new TradeDetail();
		detail.setCode(code);
		detail.setName(name);
		detail.setTradeDate(date);
		detail.setPrice(price);
		detail.setQuantity(-1 * onHands.get(code).getQuantity());
		details.add(detail);
		
		cash = cash.subtract(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量为负，所以用 substract

		onHands.remove(code);
	}
	
	public void setPrice(String code, BigDecimal price){
		TradeDetail detail = onHands.get(code);
		
		detail.setPrice(price);
	}
	
	public boolean onHand(String code){
		return onHands.containsKey(code);
	}
	
	public List<TradeDetail> getOnHands(){
		List<TradeDetail> list = new ArrayList<TradeDetail>();
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()){
			TradeDetail detail = new TradeDetail();
			detail.setTradeDate(entry.getValue().getTradeDate());
			detail.setCode(entry.getValue().getCode());
			detail.setName(entry.getValue().getName());
			detail.setCost(entry.getValue().getCost());
			detail.setPrice(entry.getValue().getPrice());
			detail.setQuantity(entry.getValue().getQuantity());
			detail.setTradeDate(entry.getValue().getTradeDate());
			list.add(detail);
		}
		
		return list;
	}
	
	public List<TradeDetail> getDetails(){
		return details;
	}
	
	public String getDetailString(){
		StringBuffer sb = new StringBuffer();
		for(TradeDetail d : details){
			sb.append(d.getTradeDate());
			sb.append(",");
			sb.append("C" + d.getCode());
			sb.append(",");
			sb.append(d.getQuantity()>0 ? d.getCost() : d.getPrice());
			sb.append(",");
			sb.append(d.getQuantity());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getAccountString(){
		StringBuffer sbp = new StringBuffer();
		for(Account a : accounts){
			sbp.append(a.getDate());
			sbp.append(",");
			sbp.append(a.getCash());
			sbp.append(",");
			sbp.append(a.getValue());
			sbp.append(",");
			sbp.append(a.getCash().add(a.getValue()));
			sbp.append("\n");
		}
		return sbp.toString();
	}
	
}
