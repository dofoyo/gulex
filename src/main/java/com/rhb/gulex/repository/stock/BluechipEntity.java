package com.rhb.gulex.repository.stock;

import java.util.ArrayList;
import java.util.List;

public class BluechipEntity {
	private String code;
	private String name;
	private List<Integer> years = new ArrayList<Integer>();
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addYear(Integer year){
		years.add(year);
	}
	public List<Integer> getYears() {
		return years;
	}
	public void setYears(List<Integer> years) {
		this.years = years;
	}

	
	
}
