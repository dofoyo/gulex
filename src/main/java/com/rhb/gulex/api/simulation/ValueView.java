package com.rhb.gulex.api.simulation;

public class ValueView {
	private String date;
	private Integer value;
	private Integer sse; //  Shanghai Stock Exchange Composite Index 上证指数
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getSse() {
		return sse;
	}
	public void setSse(Integer sse) {
		this.sse = sse;
	}
	

	
}
