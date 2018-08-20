package com.rhb.gulex.traderecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.bluechip.api.BluechipView;
import com.rhb.gulex.bluechip.repository.BluechipEntity;
import com.rhb.gulex.bluechip.repository.BluechipRepository;
import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.traderecord.api.TradeRecordDzh;
import com.rhb.gulex.traderecord.api.TradeRecordJdh;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;

@Service("TradeRecordServiceImp")
public class TradeRecordServiceImp implements TradeRecordService {
	protected static final Logger logger = LoggerFactory.getLogger(TradeRecordServiceImp.class);

	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepositoryFromDzh;

	@Autowired
	@Qualifier("TradeRecordRepositoryFromQt")
	TradeRecordRepository tradeRecordRepositoryFromQt;
	
	@Autowired
	@Qualifier("BluechipRepositoryImp")
	BluechipRepository bluechipRepository;
	
	@Autowired
	@Qualifier("BluechipServiceImp")
	BluechipService bluechipService;
	
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	
	
	Map<String,TradeRecordDTO> tradeRecordDtos = new HashMap<String,TradeRecordDTO>(); //stockcode - TradeRecordDTO

	private void init(String stockcode){
		List<TradeRecordEntity> entities = new ArrayList<TradeRecordEntity>();
		List<TradeRecordEntity> dzh = tradeRecordRepositoryFromDzh.getTradeRecordEntities(stockcode);
		if(dzh != null && dzh.size()>0) {
			entities.addAll(dzh);
		}
		
		if(stockcode.equals("sh000001") && entities.size()>1) {
			System.out.println("last date in dzh is " + entities.get(entities.size()-1).getDate());
		}
		
		if(entities.size()>0) {
			List<TradeRecordEntity> e163 = tradeRecordRepositoryFromQt.getTradeRecordEntities(stockcode,entities.get(entities.size()-1).getDate()); 
			entities.addAll(e163);
		}else {
			List<TradeRecordEntity> e163 = tradeRecordRepositoryFromQt.getTradeRecordEntities(stockcode); 
			entities.addAll(e163);
		}
		
		if(stockcode.equals("sh000001") && entities.size()>1) {
			System.out.println("last date in qt is " + entities.get(entities.size()-1).getDate());
		}
		
		TradeRecordDTO dto = new TradeRecordDTO();
		TradeRecordEntity entity = null;
		TradeRecordEntity previous = null;
		for(int i=0; i<entities.size(); i++) {
			if(i==0) {
				previous = entities.get(i);
			}else{
				previous = entities.get(i-1);
			}
			entity = entities.get(i);
			entity.setAv120(calAvaragePrice(entities.subList(0, i),entity.getPrice(),120));
			entity.setAv60(calAvaragePrice(entities.subList(0, i),entity.getPrice(),60));
			entity.setAv250(calAvaragePrice(entities.subList(0, i),entity.getPrice(),250));
			entity.setAboveAv120Days(calAboveAv120Days(entities.subList(0, i)));
			entity.setMidPrice(calMidPrice(entities.subList(0, i),entity.getPrice()));

			
			if(!previous.is60On120() && entity.is60On120() && !entity.is120On250()) {
/*				if(entity.getCode().equals("300015")) {
					System.out.println(stockcode + "," + previous.getDate().toString() + ",av60=" + entity.getAv60() + ",av120=" + entity.getAv120() + ",setBuyDay(1)");
				}*/
				entity.setBuyDay(1);
			}else if(!previous.is120On250() && entity.is60On120() && entity.is120On250()) {
/*				if(entity.getCode().equals("300015")) {
					System.out.println(stockcode + "," + previous.getDate().toString() + ",av120=" + entity.getAv120() + ",av250=" + entity.getAv250() + ",setBuyDay(2)");
				}*/
				entity.setBuyDay(2);
			}else {
				entity.setBuyDay(0);
			}
			
			dto.add(entity.getDate(), entity);
		}

		//System.out.println("trade records after change");
		//System.out.println(entities.size());
		
		tradeRecordDtos.put(stockcode, dto);
		
		
		
	}

	
	private BigDecimal calAvaragePrice(List<TradeRecordEntity> records, BigDecimal price, Integer days){
		
		BigDecimal total = price;
		int start = records.size()>days ? records.size()-days : 0;
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
			if(tr.isPriceOnAv(120)){
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
	public List<TradeRecordEntity> getTradeRecords(String stockcode, LocalDate endDate) {
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		List<TradeRecordEntity> list = new ArrayList<TradeRecordEntity>();
		List<TradeRecordEntity> entities = tradeRecordDtos.get(stockcode).getTradeRecordEntities();
		for(TradeRecordEntity entity : entities) {
			if(entity.getDate().isBefore(endDate) || entity.getDate().isEqual(endDate)) {
				list.add(entity);
			}
		}
		
		return list;
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
	public TradeRecordEntity getSimilarTradeRecordEntity(String stockcode, LocalDate date) {
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
	public TradeRecordEntity getTradeRecordEntity(String stockcode, LocalDate date) {
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
			
			
		}
		
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);
/*		if(date.equals(LocalDate.parse("2018-05-14")) && stockcode.equals("002726")){
			System.out.println(dto);			
		}
*/		
		if(dto==null) {
			return null;
		}
		return dto.getTradeRecordEntity(date);
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
			
			Integer i = entity.isPriceOnAv(120) ? 1 : 0;
			entity.setAboveAv120Days(dto.getAboveAv120Days(date) + i);
		}
	}

	@Override
	public List<TradeRecordDzh> getDzhs() {
		Map<String,TradeRecordDzh> tradeRecordDzhs = new HashMap<String,TradeRecordDzh>();
		TradeRecordDzh tradeRecordDzh;
		
		List<TradeRecordEntity> tradeRecordEntities;
		TradeRecordEntity tradeRecordEntity;
		
		Set<BluechipEntity> bluechipEntities = bluechipRepository.getBluechips();
		for(BluechipEntity bluechipEntity : bluechipEntities){
			tradeRecordEntities = tradeRecordRepositoryFromDzh.getTradeRecordEntities(bluechipEntity.getCode());
			tradeRecordDzh = new TradeRecordDzh();
			tradeRecordDzh.setCode(bluechipEntity.getCode());
			tradeRecordDzh.setName(bluechipEntity.getName());
			tradeRecordDzh.setOkYears(bluechipEntity.getOkYearString());
			tradeRecordDzh.setIpoDate(bluechipEntity.getIpoDate());

			if(tradeRecordEntities==null || tradeRecordEntities.size()==0) {
				tradeRecordDzh.setDzhDate("");
				
				//System.out.println(tradeRecordDzh);
			}else {
				tradeRecordEntity = tradeRecordEntities.get(tradeRecordEntities.size()-1);
				tradeRecordDzh.setDzhDate(tradeRecordEntity.getDate().toString());
			}
			
			tradeRecordDzhs.put(tradeRecordDzh.getCode(),tradeRecordDzh);
			
		}
		
		List<TradeRecordDzh> list = new ArrayList<TradeRecordDzh>(tradeRecordDzhs.values());
		Collections.sort(list,new Comparator<TradeRecordDzh>() {

			@Override
			public int compare(TradeRecordDzh o1, TradeRecordDzh o2) {
				return o1.getDzhDate().compareTo(o2.getDzhDate());
			}
			
		});

		return list;
	}

	@Override
	public void refresh() {
		System.out.println("TradeRecordService refresh begin......");
		Set<String> codes = new HashSet<String>(tradeRecordDtos.keySet());
		codes.add("sh000001");
		int i=0;
		for(String code : codes) {
			System.out.print(i++ + "/" + codes.size() + "\r");
			this.init(code);
		}
		System.out.println("there are " + i + " stocks' trade records refreshed.");
		
		System.out.println(".........TradeRecordService refresh end.");
	}


	@Override
	public List<LocalDate> getTradeDate(LocalDate beginDate) {
		String stockcode = "sh000001"; //以上证指数的交易日期作为历史交易日历
		
		if(tradeRecordDtos==null || !tradeRecordDtos.containsKey(stockcode)){
			init(stockcode);
		}
		
		TradeRecordDTO dto = this.getTradeRecordsDTO(stockcode);

		List<LocalDate> dates = dto.getDates(beginDate);
		
		return dates;
	}


	@Override
	public List<TradeRecordJdh> getJdhs() {
		List<TradeRecordJdh> jdhs = new ArrayList<TradeRecordJdh>();

		TradeRecordDTO tradeRecordDTO;
		List<TradeRecordEntity> entities;
		List<BluechipView> bluechips = bluechipService.getBluechipViews(LocalDate.now());
		
		for(BluechipView view : bluechips) {
			tradeRecordDTO = tradeRecordService.getTradeRecordsDTO(view.getCode());
			entities = tradeRecordDTO.getBuyDays();
			for(TradeRecordEntity entity : entities) {
				jdhs.add(new TradeRecordJdh(view.getCode(),view.getName(),entity.getDate(),entity.getBuyDay()==1 ? "60日线上穿120线" : "120日线上穿250线"));
			}
		}
		
		Collections.sort(jdhs,new Comparator<TradeRecordJdh>() {

			@Override
			public int compare(TradeRecordJdh o1, TradeRecordJdh o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
			
		});
		
		return jdhs;
	}

	
}
