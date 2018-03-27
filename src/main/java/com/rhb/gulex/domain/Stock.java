package com.rhb.gulex.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Stock {

	private String stockId;
	private String stockName;
	private LocalDate ipoDate;
	private Integer deleted = 0;
	private FinancialStatement financialStatements = new FinancialStatement();
	private MarketInfo marketInfo = new MarketInfo();
	private boolean isInitMrketInfo = false;
	
	public int getFirstPeriod(){
		return this.financialStatements.getFirstPeriod();
	}
	
	public boolean isOk(Integer year){
		return financialStatements.isOK(year);
	}
	
	public LocalDate getIpoDate() {
		return ipoDate;
	}

	public void setIpoDate(LocalDate ipoDate) {
		this.ipoDate = ipoDate;
	}

	public boolean isInitMrketInfo() {
		return isInitMrketInfo;
	}

	public void setInitMrketInfo(boolean isInitMrketInfo) {
		this.isInitMrketInfo = isInitMrketInfo;
	}

	public MarketInfo getMarketInfo() {
		return marketInfo;
	}

	public void setMarketInfo(MarketInfo marketInfo) {
		this.marketInfo = marketInfo;
	}

	public Stock(String stockid, String stockname){
		this.stockId = stockid;
		this.stockName = stockname;
	}
	
	public String getGoodPeriod(Integer year){
		return financialStatements.getGoodPeriod(year);
	}

	public Integer getLastPeriod(){
		return financialStatements.getLastPeriod();
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
	
	public List<TradeRecord> getTradeRecords(){
		return marketInfo.getTradeRecords();
	}
	
	public TradeRecord getTradeRecord(LocalDate date){
		return marketInfo.findByDate(date);
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
		this.marketInfo.addTradeRecord(date,price,this.stockName);
	}

	
	public TradeRecord getPrice(LocalDate date){
		return marketInfo.findByDate(date);
	}
	
}
