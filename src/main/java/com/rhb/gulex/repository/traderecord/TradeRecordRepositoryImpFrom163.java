package com.rhb.gulex.repository.traderecord;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.download.DownloadTradeRecord;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

@Service("TradeRecordRepositoryImpFrom163")
public class TradeRecordRepositoryImpFrom163 implements TradeRecordRepository {
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	DownloadTradeRecord downloadTradeData;
	
	
	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code) {
		String pathAndfileName = dataPath + code + "_tradedata.csv";
		
		/*
		File file = new File(pathAndfileName);
		if(!file.exists()){
			downloadTradeData.go(code);
		}*/
		
		//System.out.println(pathAndfileName);
		List<TradeRecordEntity> records = new LinkedList<TradeRecordEntity>();
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		LocalDate date = null;
		BigDecimal price = null;
		String[] columns = null;
		String[] lines = str.split("\n");
		for(int i=lines.length-1; i>1; i--){  //文档是倒序，要变成顺序
			columns = lines[i].split(",");
			
			date = ParseString.toLocalDate(columns[0]);
			price = ParseString.toBigDecimal(columns[3]);
			
			if(date!=null && price!=null && price.compareTo(BigDecimal.ZERO)==1){
				TradeRecordEntity tre = new TradeRecordEntity();
				tre.setDate(date);
				tre.setPrice(price);
				records.add(tre);
			}
		}
		return records;
	}
	
	

}
