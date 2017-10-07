package com.rhb.gulex.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockRepository {
	private String jsonfile = "d:\\stocks\\goodstocks.json";
	
	
	public List<String> getGoodStocks(){
		List<String> stocks = null;
	
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
		
		return null;
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
	
	
}
