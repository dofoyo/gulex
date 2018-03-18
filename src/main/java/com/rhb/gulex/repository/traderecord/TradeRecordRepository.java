package com.rhb.gulex.repository.traderecord;

import java.util.List;

public interface TradeRecordRepository {
	public List<TradeRecordEntity> getTradeRecordEntities(String code);

}
