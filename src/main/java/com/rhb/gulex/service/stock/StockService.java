package com.rhb.gulex.service.stock;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Bluechip;
import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.repository.stock.BluechipEntity;

public interface StockService {
	
	public Set<String> getStockCodes();
	public StockDTO getStock(String code, LocalDate date);
	public StockDTO getStock(String code);
	public List<StockDTO> getGoodStocks(LocalDate date);
	public void init();
	public void creates(Map<String,String> codeAndNames);
	
	
	public List<StockDTO> getDeletedStocks();
	public boolean isExist(String code);
	public String[] getNoReportStockCode(int Year);
	public boolean noReport(String code, Integer year);
	public void setFinancialStatements(String[] codes);
	public void setFinancialStatement(String code);
	public void setMarketInfo(String code);
	public List<TradeRecord> getTradeRecords(String code);

}
