package com.rhb.gulex.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.TradeRecord;

public interface StockService {
		
	public List<StockDTO> getGoodStocks(LocalDate date);
	public void init();
	public void creates(Map<String,String> codeAndNames);
	public boolean isAboveAvarage(String code);
	public BigDecimal getPrice(String code, LocalDate date);
	
	public List<StockDTO> getDeletedStocks();
	public boolean isExist(String code);
	public String[] getNoReportStockCode(int Year);
	public boolean noReport(String code, Integer year);
	public void setFinancialStatements(String[] codes);
	public void setFinancialStatement(String code);
	public void refreshMarketInfo(String code);
	public List<TradeRecord> getTradeRecords(String code);
	public Integer getUpProbability(String code);

}
