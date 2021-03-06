package com.rhb.gulex.traderecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.traderecord.api.TradeRecordDzh;
import com.rhb.gulex.traderecord.api.TradeRecordJdh;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;

public interface TradeRecordService {
	public LocalDate getIpoDate(String stockcode);
	
	public TradeRecordDTO getTradeRecordsDTO(String stockcode);
	
	//public BigDecimal getMidPrice(String stockcode, LocalDate date);
	
	public TradeRecordEntity getSimilarTradeRecordEntity(String stockcode, LocalDate date);
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date);
	
	public void setTradeRecordEntity(String stockcode,LocalDate date, BigDecimal price);
	
	public List<TradeRecordDzh> getDzhs();
	public List<TradeRecordJdh> getJdhs();

	
	public void refresh();
	
	public List<TradeRecordEntity> getTradeRecords(String stockcode, LocalDate endDate);
	
	public List<LocalDate> getTradeDate(LocalDate beginDate);


}
