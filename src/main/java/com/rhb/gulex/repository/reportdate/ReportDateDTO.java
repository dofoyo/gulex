package com.rhb.gulex.repository.reportdate;

import java.util.HashMap;
import java.util.Map;

public class ReportDateDTO {
	Map<Integer,String> dates = new HashMap<Integer,String>();
	
	public void add(Integer year, String date){
		dates.put(year, date);
	}
	
	public String getDate(Integer year){
		return dates.get(year);
	}
	
	public Map<Integer,String> getDates(){
		return this.dates;
	}
}
