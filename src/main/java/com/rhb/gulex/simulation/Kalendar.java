package com.rhb.gulex.simulation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rhb.gulex.download.DownloadTradeRecord;
import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;

@Component
public class Kalendar {
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	TradeRecordRepository tradeRecordRepository;

	private String code = "000001"; //以平安银行的交易记录作为历史交易日历

	public List<LocalDate> getTradeDate(){
		List<LocalDate> codes = new ArrayList<LocalDate>();
		List<TradeRecordEntity> records = tradeRecordRepository.getTradeRecordEntities(code);
		for(TradeRecordEntity record : records){
			codes.add(record.getDate());
		}
		return codes;
	}
	
	
	
}
