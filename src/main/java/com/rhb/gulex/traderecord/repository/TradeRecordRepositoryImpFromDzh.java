package com.rhb.gulex.traderecord.repository;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

@Service("TradeRecordRepositoryImpFromDzh")
public class TradeRecordRepositoryImpFromDzh implements TradeRecordRepository {
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/dzh/";
	
	
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
		for(int i=2; i<lines.length; i++){
			line = lines[i];
			cells = line.split("\t");
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			TradeRecordEntity tre = new TradeRecordEntity();
			tre.setCode(code);
			tre.setDate(LocalDate.parse(cells[0],df));
			tre.setPrice(ParseString.toBigDecimal(cells[4]));
			
			records.add(tre);

		}
		//System.out.println(records.size());

		return records;
	}

	

	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code, LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void save(List<Map<String, String>> records) {
		// TODO Auto-generated method stub
		
	}



	
	

}
