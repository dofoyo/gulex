package com.rhb.gulex.stock.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rhb.gulex.stock.api.StockDTO;

public interface StockService {
	public void init();
	public StockDTO getStock(String code);
	public List<StockDTO> getStocks();
	public void creates(Map<String,String> codeAndNames);
	public boolean isExist(String code);


	
	@Deprecated
	public Set<String> getStockCodes();
	
	
	@Deprecated
	public List<StockDTO> getDeletedStocks();
	

}
