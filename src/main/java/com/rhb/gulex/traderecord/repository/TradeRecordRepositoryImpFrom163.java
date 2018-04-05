package com.rhb.gulex.traderecord.repository;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.traderecord.spider.DownloadTradeRecord;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

import edu.emory.mathcs.backport.java.util.Collections;

@Service("TradeRecordRepositoryImpFrom163")
public class TradeRecordRepositoryImpFrom163 implements TradeRecordRepository {
	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/163/";
	
	@Autowired
	@Qualifier("DownloadTradeRecordFrom163")
	DownloadTradeRecord downloadTradeData;
	
	
	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code) {
		String pathAndfileName = dataPath + subPath + code + "_tradedata.csv";
		
		File file = new File(pathAndfileName);
		if(!file.exists()){
			System.out.println(pathAndfileName + " traderecord do NOT exist, download ....");
			downloadTradeData.go(code);
		}
		
		//System.out.println(pathAndfileName);
		List<TradeRecordEntity> records = new LinkedList<TradeRecordEntity>();
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		LocalDate date = null;
		BigDecimal price = null;
		String[] columns = null;
		String[] lines = str.split("\n");
		for(int i=lines.length-1; i>0; i--){  //文档是倒序，要变成顺序
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


	@Override
	public List<TradeRecordEntity> getTradeRecordEntities(String code, LocalDate sdate) {
		String pathAndfileName = dataPath + subPath + code + "_tradedata.csv";
		
		File file = new File(pathAndfileName);
		if(!file.exists()){
			System.out.println(pathAndfileName + " traderecord do NOT exist, download ....");
			downloadTradeData.go(code);
		}
		
		//System.out.println(pathAndfileName);
		List<TradeRecordEntity> records = new LinkedList<TradeRecordEntity>();
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		LocalDate date = null;
		BigDecimal price = null;
		String[] columns = null;
		String[] lines = str.split("\n");
		for(int i=1; i<lines.length; i++){  //将就倒序读取，
			columns = lines[i].split(",");
			
			date = ParseString.toLocalDate(columns[0]);
			price = ParseString.toBigDecimal(columns[3]);
			
			//System.out.println(date);
			
			
			if(date!=null && (date.isBefore(sdate) || date.equals(sdate))) {
				break;
			}
			
			if(date!=null && price!=null && price.compareTo(BigDecimal.ZERO)==1){
				TradeRecordEntity tre = new TradeRecordEntity();
				tre.setDate(date);
				tre.setPrice(price);
				records.add(tre);
				
			}
			

		}
		
		Collections.sort(records, new Comparator<TradeRecordEntity>() {

			@Override
			public int compare(TradeRecordEntity o1, TradeRecordEntity o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		
		return records;
	}


	

}
