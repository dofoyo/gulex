package com.rhb.gulex.service;

import java.util.Map;

public interface StockService {
		
	public void downloadFinancialStatements(boolean overwrite,String period);
	public Map<String,String> selectGoodStock(int bYear, int eYear);
}
