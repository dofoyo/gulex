package com.rhb.gulex.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Stock {

	private String stockId;
	private String stockName;
	private Integer deleted = 0;
	private LocalDate dDate = LocalDate.now();
	private FinancialStatements financialStatements = new FinancialStatements();
	private MarketInfo marketInfo = new MarketInfo();
	
	public Stock(String stockid, String stockname){
		this.stockId = stockid;
		this.stockName = stockname;
		//this.fs = new FinancialStatements(stockid,dataPath);
		//this.refresh();
		//this.trade = 
	}
	
	public void SetDdate(LocalDate date){
		this.dDate = date;
		
	}
	public LocalDate getDdate(){
		return this.dDate;
	}
	
	public boolean isGood(){
		return financialStatements.isGood();
	}
	
	public int getGoodTimes(){
		return financialStatements.getGoodTimes();
	}
	
	public String getGoodPeriodString(){
		return financialStatements.getGoodPeriodString();
	}

	public Integer getLastPeriod(){
		return financialStatements.getLastPeriod(LocalDate.now());
	}
	
	public void setBalancesheets(Map<String, BalanceSheet> balancesheets) {
		this.financialStatements.setBalancesheets(balancesheets);
	}

	public void setCashflows(Map<String, CashFlow> cashflows) {
		this.financialStatements.setCashflows(cashflows);
	}

	public void setProfitstatements(Map<String, ProfitStatement> profitstatements) {
		this.financialStatements.setProfitstatements(profitstatements);
	}

	
	public void refreshFinancialStatements(LocalDate date){
		financialStatements.refreshGoodPeriods(date);		
	}

	public void refreshMarketInfo(){
		//System.out.println("stock.refreshMarketInfo");
		marketInfo.refreshAvaragePrice();
	}

	
	public List<TradeRecord> getTradeRecords(){
		return marketInfo.getTradeRecords();
	}
	
	
	public boolean exists(String period){
		return financialStatements.exists(period);
	}
	
	public String getStockName(){
		return stockName;
	}

	public String getStockId() {
		return stockId;
	}
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public void addTradeRecord(LocalDate date,BigDecimal price) {
		//System.out.println("stock.addTradeRecord");
		this.marketInfo.addTradeRecord(date,price);
	}

	public Integer getUpProbability(){
		return marketInfo.getUpProbability();
	}
	
	public boolean isAboveAv120(){
		return marketInfo.isAboveAv120();
	}
	
	public BigDecimal getPrice(LocalDate date){
		return marketInfo.getPrice(date);
	}
	
}
