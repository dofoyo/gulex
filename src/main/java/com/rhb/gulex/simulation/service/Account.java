package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Account {
	private LocalDate date;
	private BigDecimal cash;
	private BigDecimal value;
	private Integer buyValve;
	
	
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public BigDecimal getTotal() {
		return cash.add(value);
	}
	public Integer getBuyValve() {
		return buyValve;
	}
	public void setBuyValve(Integer buyValve) {
		this.buyValve = buyValve;
	}

	
	
	
	
}
