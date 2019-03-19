package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.Collections;

public class Trader {
	protected static final Logger logger = LoggerFactory.getLogger(Trader.class);

	
	private Map<String,TradeDetail> details = new HashMap<String,TradeDetail>(); // seriesId - tradedetail
	private Map<String,TradeDetail> onHands = new HashMap<String,TradeDetail>(); // seriesId - tradedetail
	private Map<LocalDate,Account> accounts = new HashMap<LocalDate,Account>();

	private BigDecimal cash;  //现金
	
	private Integer buyValvePeriod_xx;
	
	private boolean financing;
	private boolean amountFix; //true表示固定值，false表示固定比率
	private Integer fixAmount;  //表示5万
	private Integer fixRate;  //表示百分之几
	
	
	public TradeDetail getOnHandTradeDetail(String seriesid) {
		return onHands.get(seriesid);
	}
	
	public void setAmountFix(boolean amountfix) {
		this.amountFix = amountfix;
	}

	public void setFixAmount(Integer fixAmount) {
		this.fixAmount = fixAmount;
	}

	public void setFixRate(Integer fixRate) {
		this.fixRate = fixRate;
	}

	public Trader(LocalDate date,BigDecimal cash) {
		this.cash = cash;
		Account account = new Account();
		account.setDate(date.minusDays(1));
		account.setCash(cash);
		account.setValue(new BigDecimal(0));

		accounts.put(date,account);
		
	}
	
	public void setFinancing(boolean financing) {
		this.financing = financing;
	}
	
	public List<String> getOuters(Integer limitNumber) {
		List<String> seriesids = new ArrayList<String>();
		if(onHands.size() > limitNumber) {
			Integer k = onHands.size() - limitNumber;
			List<TradeDetail> tradeDetails = new ArrayList<TradeDetail>(onHands.values());
			Collections.sort(tradeDetails,new Comparator<TradeDetail>() {

				@Override
				public int compare(TradeDetail o1, TradeDetail o2) {
					return o1.getProfitRate().compareTo(o2.getProfitRate());
				}
				
			});
			
			for(int i=0; i<k; i++) {
				seriesids.add(tradeDetails.get(i).getSeriesid());
			}
			
			
		}
		
		return seriesids;
	}
	
	public String getLowesProfitRateId() {
		String seriesid = null;

		Integer profitRate = 100;
		
		BigDecimal buyPrice;
		BigDecimal nowPrice;
		
		Integer tRate;
		
		BigDecimal value = new BigDecimal(0);
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()) {
			buyPrice = entry.getValue().getBuyCost();
			nowPrice = entry.getValue().getSellPrice();
			tRate = nowPrice.subtract(buyPrice).divide(buyPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
			if(tRate < profitRate) {
				profitRate = tRate;
				seriesid = entry.getKey();
			}
		}
		
		return seriesid;
		
	}
	
	public Integer getWinLossRatio_xxx() {
		Double winLossRatio = null;
		
		if(onHands.size()>0) {
			double win = 0.0;
			double loss = 0.0;
			BigDecimal buyPrice;
			BigDecimal nowPrice;
			
			List<TradeDetail> onHandsList = new ArrayList<TradeDetail>(onHands.values());
			Collections.sort(onHandsList, new Comparator<TradeDetail>() {

				@Override
				public int compare(TradeDetail o1, TradeDetail o2) {
					return o2.getBuyDate().compareTo(o1.getBuyDate());
				}
				
			});
			
			for(int i=0; i<onHandsList.size() && i<buyValvePeriod_xx; i++) {
				
				//System.out.println(onHandsList.get(i));/////////////////////////////////////
				
				buyPrice = onHandsList.get(i).getBuyCost();
				nowPrice = onHandsList.get(i).getSellPrice();
				
				if(nowPrice.compareTo(buyPrice)==1) {
					win ++;
				}else {
					loss ++;
				}
			}
				
			winLossRatio = win/(win+loss)*100;
					
		}
		
		return winLossRatio==null ? null : winLossRatio.intValue();
	}
	
	public void setBuyValvePeriod_xx(Integer period) {
		this.buyValvePeriod_xx = period;
	}



/*
	public Integer getOnHandLowestProfitRate(String code) {
		Integer profitRate = 0;
		
		BigDecimal buyPrice;
		BigDecimal nowPrice;
		
		Integer tRate;
		
		BigDecimal value = new BigDecimal(0);
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()) {
			if(entry.getValue().getCode().equals(code)) {
				buyPrice = entry.getValue().getBuyCost();
				nowPrice = entry.getValue().getSellPrice();
				tRate = nowPrice.subtract(buyPrice).divide(buyPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
				//if(tRate < profitRate) {
					profitRate = Math.abs(tRate);
				//}
				
			}
		}
		
		//profitRate = profitRate==100 ? 0 : profitRate;
		
		return profitRate;
		
	}
	*/
	
	
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
	
	public Integer getOnHandLowestProfitRate(String code) {
		TradeDetail td = null;
		
		Integer profitRate = 0;
		
		BigDecimal buyPrice;
		BigDecimal nowPrice;
		
		Integer tRate;
		
		BigDecimal value = new BigDecimal(0);
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()) {
			if(code.equals(entry.getValue().getCode())) {
				if(td==null || td.getBuyDate().isBefore(entry.getValue().getBuyDate())){
					td = entry.getValue();
				}
			}
		}
		
		//buyPrice = td.getBuyCost();
		//nowPrice = td.getSellPrice();
		//tRate = nowPrice.subtract(buyPrice).divide(buyPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		
		//if(td!=null && td.getProfitRate())
		
		if(td != null) {
			profitRate = Math.abs(td.getProfitRate());
			//logger.info(code + "'s profitRate = " + profitRate);
		}else {
			//logger.info(code + " is null!");
		}
		
		return profitRate;
		
	}
	
	
	public Integer getOnHandProfitRate(String code) {
		Integer profitRate = 0;
		
		BigDecimal totalCost = new BigDecimal(0);
		BigDecimal totalValue = new BigDecimal(0);
		
		BigDecimal buyPrice;
		BigDecimal nowPrice;
		Integer quantity;
		
		BigDecimal value = new BigDecimal(0);
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()) {
			if(entry.getValue().getCode().equals(code)) {
				buyPrice = entry.getValue().getBuyCost();
				nowPrice = entry.getValue().getSellPrice();
				quantity = entry.getValue().getQuantity();
				
				totalCost = totalCost.add(buyPrice.multiply(new BigDecimal(quantity)));
				totalValue = totalValue.add(nowPrice.multiply(new BigDecimal(quantity)));
				
			}
		}
		if(totalCost.compareTo(BigDecimal.ZERO)==1) {
			profitRate = totalValue.subtract(totalCost).divide(totalCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		}

		
		return profitRate;
	}
	
	public BigDecimal getTotal() {
		LocalDate date = LocalDate.now();
		
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
		return total;
		
	}
	
	public void dayReport(LocalDate date,Integer buyValve){
		BigDecimal value = new BigDecimal(0);  
		
		for(Map.Entry<String, TradeDetail> entry : onHands.entrySet()){
			value = value.add(entry.getValue().getSellPrice().multiply(new BigDecimal(entry.getValue().getQuantity())));
		}
		
		Account account = new Account();
		account.setDate(date);
		account.setCash(cash);
		account.setValue(value);
		account.setBuyValve(buyValve);
		
		accounts.put(date,account);
		
		//System.out.println("dayReport: " + date.toString() + " " + " cash = " + cash.toString() + " value = " + account.getValue().toString());

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
	
	public void buy(String code, String name, LocalDate date, BigDecimal price, String note){
		
		BigDecimal a = this.getBuyAmount(date);
		
		if(a.compareTo(new BigDecimal(0))==1){
			Integer quantity = a.divide(price,2,BigDecimal.ROUND_HALF_UP).intValue() /100 * 100;
			if(quantity > 0){
				TradeDetail detail = new TradeDetail();
				detail.setSeriesid(UUID.randomUUID().toString());
				detail.setCode(code);
				detail.setName(name);
				detail.setBuyDate(date);
				detail.setBuyCost(price);
				detail.setSellPrice(price); // 买入时就要赋值，不然无法日结
				detail.setQuantity(quantity); 
				detail.setBuynote(note);
				
				cash = cash.subtract(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量增加，现金减少
				
				onHands.put(detail.getSeriesid(),detail);			
				
			}
		}
	}
	
	public void sell(String seriesid, LocalDate date, BigDecimal price, String note){
		TradeDetail detail = onHands.get(seriesid);
		
		detail.setSellDate(date);
		detail.setSellPrice(price);
		detail.setSellnote(note);
		
		details.put(seriesid,detail);
		
		cash = cash.add(price.multiply(new BigDecimal(detail.getQuantity())));  //因为数量减少，现金增加

		onHands.remove(seriesid);
	}

	//价格已在之前赋值了
	public void sell(String seriesid, LocalDate date, String note){
		TradeDetail detail = onHands.get(seriesid);
		
		detail.setSellDate(date);
		//detail.setSellPrice(price);//
		detail.setSellnote(note);
		
		details.put(seriesid,detail);
		
		cash = cash.add(detail.getSellPrice().multiply(new BigDecimal(detail.getQuantity())));  //因为数量减少，现金增加

		onHands.remove(seriesid);
	}
	
	public void setPrice(String seriesid, BigDecimal price){
		TradeDetail detail = onHands.get(seriesid);
		
		detail.setSellPrice(price);
	}

	
	public int countOnHands(String code) {
		int flag = 0;
		for(Map.Entry<String,TradeDetail> entry : onHands.entrySet()) {
			if(code.equals(entry.getValue().getCode())) {
				flag++;
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
	
	public List<TradeDetail> getSimulationDetails(){
		List<TradeDetail> list = new ArrayList<TradeDetail>(this.details.values());
		list.addAll(this.onHands.values());
		return list;
	}
	
	public String getDetailString(){
		StringBuffer sb = new StringBuffer();
		sb.append("code");
		sb.append(",");
		sb.append("name");
		sb.append(",");
		sb.append("quantity");
		sb.append(",");
		sb.append("buydate");
		sb.append(",");
		sb.append("buyprice");
		sb.append(",");
		sb.append("selldate");
		sb.append(",");
		sb.append("sellprice");
		sb.append(",");
		sb.append("cost");
		sb.append(",");
		sb.append("value");
		sb.append(",");
		sb.append("profit");
		sb.append(",");
		sb.append("highest");
		sb.append(",");
		sb.append("highestRate");
		sb.append(",");
		sb.append("lowest");
		sb.append(",");
		sb.append("lowestRate");
		sb.append(",");
		sb.append("buynote");		
		sb.append(",");
		sb.append("sellnote");
		sb.append("\n");
		
		List<TradeDetail> list = new ArrayList<TradeDetail>(this.getDetails().values());
		list.addAll(this.getOnHands().values());
		
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
			sb.append(",");
			sb.append(d.getBuyCost().multiply(new BigDecimal(d.getQuantity())));
			sb.append(",");
			sb.append(d.getSellPrice().multiply(new BigDecimal(d.getQuantity())));
			sb.append(",");
			sb.append(d.getSellPrice().subtract(d.getBuyCost()).multiply(new BigDecimal(d.getQuantity())));
			sb.append(",");
			sb.append(d.getHighestPrice());
			sb.append(",");
			sb.append(d.getHighestRate());
			sb.append(",");
			sb.append(d.getLowestPrice());
			sb.append(",");
			sb.append(d.getLowestRate());
			sb.append(",");
			sb.append(d.getBuynote());			
			sb.append(",");
			sb.append(d.getSellnote());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getAccountString(){
		StringBuffer sbp = new StringBuffer();
		sbp.append("date");
		sbp.append(",");
		sbp.append("cash");
		sbp.append(",");
		sbp.append("value");
		sbp.append(",");
		sbp.append("total");
		sbp.append(",");
		sbp.append("buyValve");
		sbp.append("\n");
		
		List<Account> list = this.getAccounts();
		for(Account a : list){
			sbp.append(a.getDate());
			sbp.append(",");
			sbp.append(a.getCash());
			sbp.append(",");
			sbp.append(a.getValue());
			sbp.append(",");
			sbp.append(a.getCash().add(a.getValue()));
			sbp.append(",");
			sbp.append(a.getBuyValve());
			sbp.append("\n");
		}
		return sbp.toString();
	}
	
	public BigDecimal getBuyAmount(LocalDate date) {
		BigDecimal amount;

		if(amountFix) {
			amount = new BigDecimal(fixAmount);
		}else {
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
			
			amount = total.multiply(new BigDecimal(fixRate)).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
			
		}
		
		if(!financing) {
			amount = amount.compareTo(cash)==1 ? cash : amount;
		}
		
		return amount;  
		
	}
	
}
