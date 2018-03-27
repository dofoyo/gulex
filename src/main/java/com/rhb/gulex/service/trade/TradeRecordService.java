package com.rhb.gulex.service.trade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.repository.traderecord.TradeRecordEntity;

public interface TradeRecordService {
	public LocalDate getIpoDate(String stockcode);
	public TradeRecordDTO getTradeRecordsDTO(String stockcode);
	public BigDecimal getMidPrice(String stockcode, LocalDate date);
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date);

}
