package com.rhb.gulex.traderecord.repository;

import java.time.LocalDate;
import java.util.List;

public interface TradeRecordRepository {
	public List<TradeRecordEntity> getTradeRecordEntities(String code);
	public List<TradeRecordEntity> getTradeRecordEntities(String code,LocalDate date);

}
