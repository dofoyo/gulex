package com.rhb.gulex.traderecord.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TradeRecordRepository {
	public List<TradeRecordEntity> getTradeRecordEntities(String code);
	public List<TradeRecordEntity> getTradeRecordEntities(String code,LocalDate beginDate);
	public void save(List<Map<String,String>> records);

}
