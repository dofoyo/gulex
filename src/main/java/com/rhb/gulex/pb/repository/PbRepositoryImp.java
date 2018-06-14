package com.rhb.gulex.pb.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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

@Service("PbRepositoryImp")
public class PbRepositoryImp implements PbRepository {
	@Value("${dataPath}")
	private String dataPath;
	private String filename = "pbs.json";

	
	@Autowired
	@Qualifier("DownloadPBFromCsindex")
	DownloadPB sownloadPB;
	
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
		
		LocalDate lastDate = this.getLastDate().plusDays(1);
		LocalDate now = LocalDate.now();
		
		while(lastDate.isBefore(now)) {
			PbEntity entity =  sownloadPB.download(lastDate.toString());
			//System.out.println(entity);
			if(entity != null) {
				entities.add(entity);			
			}
			lastDate = lastDate.plusDays(1);
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
	
	
	private LocalDate getLastDate() {
		String date = this.entities.get(this.entities.size()-1).getDate();
		return LocalDate.parse(date);
		
	}

}
