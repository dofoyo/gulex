package com.rhb.gulex.repository.stock;

import java.util.List;
import java.util.Set;

public interface StockRepository {
	public String[] getStockCodes();
	public Set<StockEntity> getStocks();
	public void save(Set<StockEntity> stocks);
	
	public void SaveBluechip(List<BluechipEntity> list);
	public List<BluechipEntity> getBluechips();
}
