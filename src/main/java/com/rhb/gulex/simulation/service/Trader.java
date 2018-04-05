package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.emory.mathcs.backport.java.util.Collections;

public class Trader {
	private Map<String,TradeDetail> details = new HashMap<String,TradeDetail>(); // seriesId - tradedetail
	private Map<String,TradeDetail> onHands = new HashMap<String,TradeDetail>(); // seriesId - tradedetail
	private Map<LocalDate,Account> accounts = new HashMap<LocalDate,Account>();

	//private  BigDecimal amount; //每次买入金额
	private  BigDecimal cash;  //现金
	//private boolean overdraft;
	
/*	public void setOverdraft(boolean flag){
		this.overdraft = flag;
	}*/
	
	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

/*	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}*/

	public void dayReport(LocalDate date){
		BigDecimal value = new BigDecimal(0);  
		
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()){
			value = value.add(entry.getValue().getSellPrice().multiply(new BigDecimal(entry.getValue().getQuantity())));
		}
		
		Account account = new Account();
		account.setDate(date);
		account.setCash(cash);
		account.setValue(value);
		
		accounts.put(date,account);
		
		//System.out.println(date.toString() + " " + " cash = " + cash.toString() + " value = " + account.getValue().toString());

	}
	
	public List<Account> getAccounts(){
		List<Account> list = new ArrayList<Account>();
		for(Map.Entry<LocalDate, Account> entry : accounts.entrySet()) {
			list.add(entry.getValue());
		}
		
		Collections.sort(list, new Comparator<Account>() {

			@Override
			public int compare(Account o1, Account o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		return list;
	}
	
	public void buy(String code, String name, LocalDate date, BigDecimal price){
/*		BigDecimal a = cash.compareTo(amount)==1 ? amount : cash; //不能透支 
		if(overdraft){
			a = amount; //允许透支
		}*/
		
		BigDecimal a = this.getAmount(date);
		if(a.compareTo(new BigDecimal(0))==1){
			Integer quantity = a.divide(price,2,BigDecimal.ROUND_HALF_UP).intValue() /100 * 100;
			if(quantity > 0){
				TradeDetail detail = new TradeDetail();
				detail.setSeriesid(UUID.randomUUID().toString());
				detail.setCode(code);
				detail.setName(name);
				detail.setBuyDate(date);
				detail.setBuyCost(price);
				detail.setQuantity(quantity); 
				
				//details.put(seriesid,detail);
				
				cash = cash.subtract(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量增加，现金减少
				
				onHands.put(detail.getSeriesid(),detail);			
				
			}
		}
	}
	
	public void sell(String seriesid, LocalDate date, BigDecimal price){
		TradeDetail detail = onHands.get(seriesid);
		
		detail.setSellDate(date);
		detail.setSellPrice(price);
		
		details.put(seriesid,detail);
		
		cash = cash.add(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量减少，现金增加

		onHands.remove(seriesid);
	}
	
	public void setPrice(String seriesid, BigDecimal price){
		TradeDetail detail = onHands.get(seriesid);
		
		detail.setSellPrice(price);
	}
	
	public boolean onHand(String code){
		boolean flag = false;
		for(Map.Entry<String,TradeDetail> entry : onHands.entrySet()) {
			if(code.equals(entry.getValue().getCode())) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public List<TradeDetail> getOnHandsList(){

		return new ArrayList<TradeDetail>(this.onHands.values());
	}
	
	public Map<String,TradeDetail> getOnHands(){
		return this.onHands;
	}
	
	public Map<String,TradeDetail> getDetails(){
		return this.details;
	}
	
	public String getDetailString(){
		StringBuffer sb = new StringBuffer();
		List<TradeDetail> list = new ArrayList<TradeDetail>(this.getDetails().values());
		
		Collections.sort(list,new Comparator<TradeDetail>() {

			@Override
			public int compare(TradeDetail o1, TradeDetail o2) {
				return o1.getBuyDate().compareTo(o2.getBuyDate());
			}
			
		});
		for(TradeDetail d : list){
			sb.append(d.getCode());
			sb.append(",");
			sb.append(d.getName());
			sb.append(",");
			sb.append(d.getQuantity());
			sb.append(",");
			sb.append(d.getBuyDate());
			sb.append(",");
			sb.append(d.getBuyCost());
			sb.append(",");
			sb.append(d.getSellDate());
			sb.append(",");
			sb.append(d.getSellPrice());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getAccountString(){
		StringBuffer sbp = new StringBuffer();
		List<Account> list = this.getAccounts();
		for(Account a : list){
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
	
	public BigDecimal getAmount(LocalDate date) {
		BigDecimal total;
		Account account = this.accounts.get(date);
		for(int i=0; account==null && i<this.accounts.size(); i++) {
			date = date.minusDays(1);
			account = this.accounts.get(date);
		}
		
		if(account!=null) {
			total = account.getTotal();
		}else {
			total = new BigDecimal(0);
		}
		
		return total.divide(new BigDecimal(20),2,BigDecimal.ROUND_HALF_UP);  // total的 5% 或 1/20
		
	}
	
}
