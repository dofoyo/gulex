package com.rhb.gulex.bluechip.repository;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service("BluechipRepositoryImp")
public class BluechipRepositoryImp implements BluechipRepository {
	@Value("${dataPath}")
	private String dataPath;
	
	private static final String filename = "bluechips.json";

	@Override
	public void save(Collection<BluechipEntity> bluechips) {
		writeToFile(dataPath+filename, bluechips);
	}

	@Override
	public Set<BluechipEntity> getBluechips() {
		Set<BluechipEntity> bluechips = null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		try {
			bluechips = mapper.readValue(new File(dataPath + filename), new TypeReference<Set<BluechipEntity>>() {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bluechips;
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
