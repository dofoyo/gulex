package com.rhb.gulex.bluechip.api;

public class BluechipView {
	private String code;
	private String name;
	private Integer upProbability = -1;
	private String okYears;
	private String date;
	
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

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getUpProbability() {
		return upProbability;
	}
	public void setUpProbability(Integer upProbability) {
		this.upProbability = upProbability;
	}
	public String getOkYears() {
		return okYears;
	}
	public void setOkYears(String okYears) {
		this.okYears = okYears;
	}
	@Override
	public String toString() {
		return "BluechipView [code=" + code + ", name=" + name + ", upProbability=" + upProbability + ", okYears="
				+ okYears + ", date=" + date + "]";
	}


	
	
	
}
