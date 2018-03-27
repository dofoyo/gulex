package com.rhb.gulex.repository.reportdate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("ReportDateRepositoryImp")
public class ReportDateRepositoryImp implements ReportDateRepository{
	@Value("${dataPath}")
	private String dataPath;
	
	private static final String filename = "reportdates.json";
	
	private Map<String,ReportDateDTO> dates = null;
	
	@Override
	public void init(){
		System.out.println("ReportDateRepositoryImp init....");
		dates = new HashMap<String,ReportDateDTO>();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, ReportDateEntity.class); 
		try {
			List<ReportDateEntity> reportDates  = mapper.readValue(new File(dataPath+filename), javaType);
			for(ReportDateEntity pde : reportDates){
				if(dates.containsKey(pde.getStockCode())){
					dates.get(pde.getStockCode()).add(pde.getYear(), pde.getReportdate());
				}else{
					ReportDateDTO dto = new ReportDateDTO();
					dto.add(pde.getYear(), pde.getReportdate());
					dates.put(pde.getStockCode(), dto);
				}
			}
			//reportDates = mapper.readValue(dataPath+filename, new TypeReference<List<ReportDateEntity>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		System.out.println("ReportDateRepositoryImp end....");

	}
	

	@Override
	public String getReportDate(String code, Integer year) {
		if(dates == null){
			this.init();
		}
		String date = null;
		
		ReportDateDTO dto = dates.get(code);
		if(dto!=null && dto.getDate(year)!=null){
			date = dto.getDate(year);
		}
		
		return date;
	}



	@Override
	public Map<Integer, String> getReportDates(String stockcode) {
		if(dates == null){
			this.init();
		}
		
		if(dates.get(stockcode)==null){
			System.out.println(stockcode + " has no reportDate!!!");
		}
		return dates.get(stockcode).getDates();
	}
}
