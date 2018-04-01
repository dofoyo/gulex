package com.rhb.gulex.traderecord.service;

import java.time.LocalDate;

import com.rhb.gulex.traderecord.repository.TradeRecordEntity;

public interface TradeRecordService {
	public LocalDate getIpoDate(String stockcode);
	public TradeRecordDTO getTradeRecordsDTO(String stockcode);
	//public BigDecimal getMidPrice(String stockcode, LocalDate date);
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date);
	

}
