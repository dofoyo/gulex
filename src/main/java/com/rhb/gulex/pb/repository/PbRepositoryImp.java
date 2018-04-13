package com.rhb.gulex.pb.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhb.gulex.pb.spider.DownloadPB;
import com.rhb.gulex.pb.spider.PbEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;

@Service("PbRepositoryImp")
public class PbRepositoryImp implements PbRepository {
	@Value("${dataPath}")
	private String dataPath;
	private String filename = "pbs.json";

	
	@Autowired
	@Qualifier("DownloadPBFromCsindex")
	DownloadPB sownloadPB;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFrom163")
	TradeRecordRepository tradeRecordRepositoryFrom163;
	
	private List<PbEntity> entities = null;
	
	@Override
	public List<PbEntity> getPbEntities() {
		if(entities == null) {
			this.init();
		}
		
		return entities;
	}
	
	@Override
	public void init() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PbEntity.class); 
		
		try {
			entities  = mapper.readValue(new File(dataPath+filename), javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}	

	}

	@Override
	public void download() {
		if(entities == null) {
			this.init();
		}
		
		LocalDate lastDate = this.getLastDate();
		
		List<LocalDate> dates = this.getTradeDate();
		
		for(LocalDate date : dates) {
			//System.out.println(date.toString());
			if(date.isAfter(lastDate)) {
				PbEntity entity =  sownloadPB.download(date.toString());
				//System.out.println(entity);
				entities.add(entity);
			}
		}
		
		writeToFile(dataPath+filename, entities);
		
	}
	
	private void writeToFile(String jsonfile, List<PbEntity> object){
		ObjectMapper mapper = new ObjectMapper();
    	try {
			mapper.writeValue(new File(jsonfile),object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<LocalDate> getTradeDate(){
		LocalDate beginDate = LocalDate.parse("2018-04-04");
		String code = "sh000001"; //以上证指数的交易日期作为历史交易日历
		List<LocalDate> dates = new ArrayList<LocalDate>();
		List<TradeRecordEntity> records = tradeRecordRepositoryFrom163.getTradeRecordEntities(code);
		for(TradeRecordEntity record : records){
			//System.out.print(record.getDate());
			if(record.getDate().isAfter(beginDate)){
				dates.add(record.getDate());
				//System.out.println("   y");
			}
		}
		return dates;
	}
	
	private LocalDate getLastDate() {
		String date = this.entities.get(this.entities.size()-1).getDate();
		return LocalDate.parse(date);
		
	}

}
