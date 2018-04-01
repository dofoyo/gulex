package com.rhb.gulex.simulation.api;

import java.math.BigDecimal;

public class ValueView {
	private String date;
	private BigDecimal value;
	private BigDecimal cash;
	private Integer sse; //  Shanghai Stock Exchange Composite Index 上证指数
	
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	public Integer getSse() {
		return sse;
	}
	public void setSse(Integer sse) {
		this.sse = sse;
	}
	
	public BigDecimal getTotal() {
		return cash.add(value);
	}
	

	

	
}
