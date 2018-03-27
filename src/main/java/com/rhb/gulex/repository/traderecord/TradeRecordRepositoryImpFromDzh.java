package com.rhb.gulex.repository.traderecord;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

@Service("TradeRecordRepositoryImpFromDzh")
public class TradeRecordRepositoryImpFromDzh implements TradeRecordRepository {
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "\\trade\\dzh\\";
	
	@Autowired
	DownloadTradeRecord downloadTradeData;
	
	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code) {
		String pathAndfileName = dataPath + subPath + code + ".txt";
		
		File file = new File(pathAndfileName);
		if(!file.exists()){
			System.out.println(pathAndfileName + " do NOT exist.");
			return null;
		}
		
		List<TradeRecordEntity> records = new ArrayList<TradeRecordEntity>();
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		String[] lines = str.split("\n");
		String line;
		String[] cells;
		LocalDate date = null;
		BigDecimal price = null;
		for(int i=2; i<lines.length; i++){
			line = lines[i];
			cells = line.split("\t");
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			TradeRecordEntity tre = new TradeRecordEntity();
			tre.setDate(LocalDate.parse(cells[0],df));
			tre.setPrice(ParseString.toBigDecimal(cells[4]));
			
			records.add(tre);

			tre.setAv120(calAv120(records));
			tre.setAboveAv120Days(calAboveAv120Days(records));
			tre.setMidPrice(calMidPrice(records));
		}

		return records;
	}

	private BigDecimal calAv120(List<TradeRecordEntity> records){
		BigDecimal total = new BigDecimal(0);
		int start = records.size()>120 ? records.size()-120 : 0;
		List<TradeRecordEntity> list = records.subList(start, records.size());
		for(TradeRecordEntity tr : list){
			total = total.add(tr.getPrice());
		}
		int i = records.size() - start;
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
	
	private BigDecimal calMidPrice(List<TradeRecordEntity> records){
		Set<BigDecimal> prices = new HashSet<BigDecimal>();
		for(TradeRecordEntity entity : records){
			prices.add(entity.getPrice());
		}
		
		List<BigDecimal> list = new ArrayList<BigDecimal>(prices);
		
		Collections.sort(list);
		
		return list.get(prices.size()/2);
	}
	
	

}
