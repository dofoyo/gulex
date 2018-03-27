package com.rhb.gulex.service.trade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;

@Service("TradeRecordServiceImp")
public class TradeRecordServiceImp implements TradeRecordService {

	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepository;
	
	Map<String,TradeRecordDTO> tradeRecordDtos = new HashMap<String,TradeRecordDTO>();

	private void init(String stockcode){
		List<TradeRecordEntity> TradeRecordEntities = tradeRecordRepository.getTradeRecordEntities(stockcode);
		
		if(TradeRecordEntities != null && TradeRecordEntities.size()>0){
			TradeRecordDTO dto = new TradeRecordDTO();
			for(TradeRecordEntity entity: TradeRecordEntities){
				dto.add(entity.getDate(), entity);
			}		
			tradeRecordDtos.put(stockcode, dto);
		}
	}


	@Override
	public TradeRecordDTO getTradeRecordsDTO(String stockcode) {
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		return tradeRecordDtos.get(stockcode);
	}
	
	@Override
	public LocalDate getIpoDate(String stockcode) {
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);

		if(dto == null){
			System.out.println("还未有成交记录! 请事先准备好！");
			return null;
		}else{
			return dto.getIpoDate();
		}
		
	}


	@Override
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date) {
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);
		return dto.getTradeRecordEntity(date);
	}

	@Override
	public BigDecimal getMidPrice(String stockcode, LocalDate date) {
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);
		
		return dto.getMidPrice(date);
	}
	
	
	


}
