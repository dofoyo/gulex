package com.rhb.gulex.repository.stock;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhb.gulex.util.FileUtil;

@Service("StockRepositoryImp")
public class StockRepositoryImp implements StockRepository{

	@Value("${dataPath}")
	private String dataPath;
	
	private static final String filename = "stocks.json";
	
	public List<String> getGoodStocks(){
		List<String> stocks = null;
		String jsonfile = dataPath + "goodstocks.json";

		ObjectMapper mapper = new ObjectMapper();
    	try {
        	JavaType javatype =  mapper.getTypeFactory().constructParametricType(List.class,String.class); 
        	stocks =  (List<String>)mapper.readValue(new File(jsonfile), javatype);

    	} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stocks;
	}
	
	public void saveGoodStocks(List<String> stocks){
		
	}
	
	
	private void writeToFile(String jsonfile, Object object){
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

	@Override
	public String[] getStockCodes() {
		Set<String> codes = new HashSet<String>();
		List<File> files = FileUtil.getFiles(dataPath, "xls", true);
		
		for(File file : files){
			codes.add(file.getName().substring(0,6));
		}
		
		return codes.toArray(new String[codes.size()]);
	}
	
	@Override
	public void save(Set<StockEntity> stocks) {
		writeToFile(dataPath + filename,stocks);
	}

	@Override
	public Set<StockEntity> getStocks() {
		Set<StockEntity> stocks = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

		try {
			stocks = mapper.readValue(new File(dataPath + filename),new TypeReference<Set<StockEntity>>() {});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stocks;
	}

	@Override
	public void SaveBluechip(List<BluechipEntity> list) {
		writeToFile(dataPath + "bluechips.json", list);
		
	}

	@Override
	public List<BluechipEntity> getBluechips() {
		List<BluechipEntity> entities = null;
		String jsonfile = dataPath + "bluechips.json";

		ObjectMapper mapper = new ObjectMapper();
    	try {
        	JavaType javatype =  mapper.getTypeFactory().constructParametricType(List.class,BluechipEntity.class); 
        	entities =  (List<BluechipEntity>)mapper.readValue(new File(jsonfile), javatype);

    	} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entities;
	}
	
}
