package com.rhb.gulex.repository.stock;

import java.util.Set;

public interface StockRepository {
	public String[] getStockCodes();
	public Set<StockEntity> getStocks();
	public void save(Set<StockEntity> stocks);
}
