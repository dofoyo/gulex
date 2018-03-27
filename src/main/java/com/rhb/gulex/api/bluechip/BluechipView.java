package com.rhb.gulex.api.bluechip;

public class BluechipView {
	private String code;
	private String name;
	private Integer upProbability;
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
	@Override
	public String toString() {
		return "BluechipView [code=" + code + ", name=" + name + ", upProbability=" + upProbability + ", date=" + date
				+ "]";
	}

	
	
	
}
