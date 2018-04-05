package com.rhb.gulex.traderecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.bluechip.repository.BluechipEntity;
import com.rhb.gulex.bluechip.repository.BluechipRepository;
import com.rhb.gulex.traderecord.api.TradeRecordDzh;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;

@Service("TradeRecordServiceImp")
public class TradeRecordServiceImp implements TradeRecordService {

	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepositoryFromDzh;

	@Autowired
	@Qualifier("TradeRecordRepositoryImpFrom163")
	TradeRecordRepository tradeRecordRepositoryFrom163;
	
	@Autowired
	@Qualifier("BluechipRepositoryImp")
	BluechipRepository bluechipRepository;
	
	Map<String,TradeRecordDTO> tradeRecordDtos = new HashMap<String,TradeRecordDTO>();

	private void init(String stockcode){
		List<TradeRecordEntity> entities = new ArrayList<TradeRecordEntity>();
		List<TradeRecordEntity> dzh = tradeRecordRepositoryFromDzh.getTradeRecordEntities(stockcode);
		if(dzh != null && dzh.size()>0) {
			entities.addAll(dzh);
		}
		if(entities.size()>0) {
			List<TradeRecordEntity> e163 = tradeRecordRepositoryFrom163.getTradeRecordEntities(stockcode,entities.get(entities.size()-1).getDate()); 
			entities.addAll(e163);
		}else {
			List<TradeRecordEntity> e163 = tradeRecordRepositoryFrom163.getTradeRecordEntities(stockcode); 
			entities.addAll(e163);
		}
		
		TradeRecordDTO dto = new TradeRecordDTO();
		TradeRecordEntity entity;
		for(int i=0; i<entities.size(); i++) {
			entity = entities.get(i);

			entity.setAv120(calAv120(entities.subList(0, i),entity.getPrice()));
			entity.setAboveAv120Days(calAboveAv120Days(entities.subList(0, i)));
			entity.setMidPrice(calMidPrice(entities.subList(0, i),entity.getPrice()));

			//System.out.println(entity);
			dto.add(entity.getDate(), entity);
			
		}

		//System.out.println("trade records after change");
		//System.out.println(entities.size());
		
		tradeRecordDtos.put(stockcode, dto);
	}
	
	private BigDecimal calAv120(List<TradeRecordEntity> records, BigDecimal price){
		
		BigDecimal total = price;
		int start = records.size()>120 ? records.size()-120 : 0;
		List<TradeRecordEntity> list = records.subList(start, records.size());
		for(TradeRecordEntity tr : list){
			total = total.add(tr.getPrice());
		}
		//System.out.println("total=" + total);
		int i = records.size() - start + 1;
		return total.divide(new BigDecimal(i),2,BigDecimal.ROUND_HALF_UP);
	}
	
	private Integer calAboveAv120Days(List<TradeRecordEntity> records){
		int above = 0;
		int start = records.size()>100 ? records.size()-100 : 0;
		List<TradeRecordEntity> list = records.subList(start, records.size());
		for(TradeRecordEntity tr : list){
			if(tr.isPriceOnAvarage()){
				above++;
			}
		}
		return above;
	}
	
	private BigDecimal calMidPrice(List<TradeRecordEntity> records,BigDecimal price){
		Set<BigDecimal> prices = new HashSet<BigDecimal>();
		prices.add(price);
		for(TradeRecordEntity entity : records){
			prices.add(entity.getPrice());
		}
		
		List<BigDecimal> list = new ArrayList<BigDecimal>(prices);
		
		Collections.sort(list);
		
		return list.get(prices.size()/2);
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
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);

		if(dto == null){
			System.out.println(stockcode + "还未有成交记录! 请事先准备好！");
			return null;
		}else{
			return dto.getIpoDate();
		}
		
	}


	@Override
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date) {
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);
		if(dto==null) {
			return null;
		}
		return dto.getSimilarTradeRecordEntity(date);
	}

	@Override
	public void setTradeRecordEntity(String stockcode,LocalDate date, BigDecimal price) {
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);
		if(dto==null) {
			System.out.println(stockcode + "还未有成交记录! 请事先准备好！");
			
		}else {
			TradeRecordEntity entity = dto.getTradeRecordEntity(date);
			
			if(entity != null) {
				entity.setPrice(price);
			}else {
				entity = new TradeRecordEntity();
				entity.setDate(date);
				entity.setPrice(price);
				dto.add(date, entity);
			}

			
			Map<String,Object> total119 = dto.getTotalOf119(date);
			Integer quantity = (Integer)total119.get("quantity");
			BigDecimal total = (BigDecimal)total119.get("total");
			entity.setAv120(total.add(price).divide(new BigDecimal(quantity+1),2,BigDecimal.ROUND_HALF_UP));
			
			entity.setMidPrice(dto.getMidPrice(date, price));
			
			Integer i = entity.isPriceOnAvarage() ? 1 : 0;
			entity.setAboveAv120Days(dto.getAboveAv120Days(date) + i);
		}
	}

	@Override
	public List<TradeRecordDzh> getNoDzh() {
		List<TradeRecordDzh> list = new ArrayList<TradeRecordDzh>();
		TradeRecordDzh tradeRecordDzh;
		
		List<TradeRecordEntity> tradeRecordEntitys;
		
		Set<BluechipEntity> bluechipEntities = bluechipRepository.getBluechips();
		for(BluechipEntity bluechipEntity : bluechipEntities){
			tradeRecordEntitys = tradeRecordRepositoryFromDzh.getTradeRecordEntities(bluechipEntity.getCode());
			if(tradeRecordEntitys == null) {
				tradeRecordDzh = new TradeRecordDzh();
				tradeRecordDzh.setCode(bluechipEntity.getCode());
				tradeRecordDzh.setName(bluechipEntity.getName());
				
				list.add(tradeRecordDzh);
			}
			
		}

		return list;
	}

	
}