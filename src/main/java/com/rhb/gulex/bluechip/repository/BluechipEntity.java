package com.rhb.gulex.bluechip.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BluechipEntity {
	private String code;
	private String name;
	private Map<Integer,String> reportDates = new HashMap<Integer,String>();
	private Set<Integer> okYears = new HashSet<Integer>();
	private String IpoDate;
	
	
	public void addOkYear(Integer year){
		this.okYears.add(year);
	}
	
	public boolean isOk(Integer year){
		return okYears.contains(year);
	}

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

	public Map<Integer, String> getReportDates() {
		return reportDates;
	}

	public void setReportDates(Map<Integer, String> reportDates) {
		this.reportDates = reportDates;
	}

	public Set<Integer> getOkYears() {
		return okYears;
	}
	
	public Set<Integer> getOkYears(Integer year){
		Set<Integer> years = new HashSet<Integer>();
		for(Integer i : okYears) {
			if(i>=year) {
				years.add(i);
			}
		}
		return years;
	}
	
	public String getOkYearString() {
		StringBuffer sb = new StringBuffer();
		for(Integer year : this.okYears) {
			sb.append(year);
			sb.append(",");
		}
		return sb.toString();
	}

	public void setOkYears(Set<Integer> okYears) {
		this.okYears = okYears;
	}

	public String getIpoDate() {
		return IpoDate;
	}

	public void setIpoDate(String ipoDate) {
		IpoDate = ipoDate;
	}

	@Override
	public String toString() {
		return "BluechipEntity [code=" + code + ", name=" + name + ", reportDates=" + reportDates + ", okYears="
				+ okYears + ", IpoDate=" + IpoDate + "]";
	}
}
