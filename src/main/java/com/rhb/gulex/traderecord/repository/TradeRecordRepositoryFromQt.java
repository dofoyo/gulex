package com.rhb.gulex.traderecord.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

import edu.emory.mathcs.backport.java.util.Collections;


@Service("TradeRecordRepositoryFromQt")
public class TradeRecordRepositoryFromQt implements TradeRecordRepository {

	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/";
	
	private Map<String,Map<String,String>> codeDatePrices = null;  // code - date - price
	
	private void init() {
		System.out.println("TradeRecordRepositoryFromQt init begin.......");
		
		codeDatePrices = new HashMap<String,Map<String,String>>();
		Map<String,String> datePrices;
		
		String pathAndfileName = dataPath + subPath + "qt.csv";
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		String date = null;
		String price = null;
		String code = null;
		String[] columns = null;
		String[] lines = str.split("\n");
		for(int i=0; i<lines.length; i++){
			if(!lines[i].trim().isEmpty()) {
				columns = lines[i].split(",");
				
				date = columns[0];
				code = columns[1];
				price =columns[2];
				
				if(!codeDatePrices.containsKey(code)) {
					codeDatePrices.put(code, new HashMap<String,String>());
				}
				
				datePrices = codeDatePrices.get(code);
				datePrices.put(date, price);
			}
		}
		
		System.out.println("..........TradeRecordRepositoryFromQt init end.");

	}
	
	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code) {
		if(codeDatePrices == null) {
			this.init();
		}
		
		List<TradeRecordEntity> list = new ArrayList<TradeRecordEntity>();
		
		Map<String,String> datePrices = codeDatePrices.get(code);
		if(datePrices != null) {
			TradeRecordEntity entity;
			for(Map.Entry<String, String> entry : datePrices.entrySet()) {
				entity = new TradeRecordEntity();
				entity.setCode(code);
				entity.setDate(LocalDate.parse(entry.getKey()));
				entity.setPrice(new BigDecimal(entry.getValue()));
				
				list.add(entity);
			}
			
			Collections.sort(list,new Comparator<TradeRecordEntity>() {

				@Override
				public int compare(TradeRecordEntity o1, TradeRecordEntity o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});			
		}
		
		return list;
	}

	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code, LocalDate beginDate) {
		if(codeDatePrices == null) {
			this.init();
		}
		
		List<TradeRecordEntity> list = new ArrayList<TradeRecordEntity>();
		
		Map<String,String> datePrices = codeDatePrices.get(code);
		if(datePrices != null) {
			TradeRecordEntity entity;
			LocalDate date;
			for(Map.Entry<String, String> entry : datePrices.entrySet()) {
				date = LocalDate.parse(entry.getKey());
				if(date!=null && date.isAfter(beginDate)) {
					entity = new TradeRecordEntity();
					entity.setCode(code);
					entity.setDate(date);
					entity.setPrice(new BigDecimal(entry.getValue()));
					
					list.add(entity);				
				}

			}
			
			Collections.sort(list,new Comparator<TradeRecordEntity>() {

				@Override
				public int compare(TradeRecordEntity o1, TradeRecordEntity o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
				
			});
		}

		return list;
	}

	@Override
	public void save(List<Map<String, String>> records) {
		if(codeDatePrices == null) {
			this.init();
		}
		Map<String,String> datePrices;
		String code;
		String date;
		String price;
		for(Map<String,String> record : records) {
			code = record.get("code");
			date = record.get("date");
			price = record.get("price");
			
			if(!codeDatePrices.containsKey(code)) {
				codeDatePrices.put(code, new HashMap<String,String>());
			}
			
			datePrices = codeDatePrices.get(code);
			datePrices.put(date, price);
		}
		
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, Map<String,String>> entry : codeDatePrices.entrySet()) {
			for(Map.Entry<String,String> en : entry.getValue().entrySet()) {
				sb.append(en.getKey());
				sb.append(",");
				sb.append(entry.getKey());
				sb.append(",");
				sb.append(en.getValue());
				sb.append("\n");

			}
			
		}
		
		String pathAndfileName = dataPath + subPath + "qt.csv";
		
		FileUtil.writeTextFile(pathAndfileName, sb.toString(), false);
		
	}


}
