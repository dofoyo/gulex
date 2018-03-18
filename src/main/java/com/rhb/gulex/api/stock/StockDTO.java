package com.rhb.gulex.api.stock;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockDTO {
	private String code;
	private String name;
	private String goodPeriod;
	private Integer goodTimes;
	private Integer lastPeriod;
	private Integer upProbability;
	private boolean isAboveAv120;
	private BigDecimal price;
	private LocalDate date;
	
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public boolean isAboveAv120() {
		return isAboveAv120;
	}
	public void setAboveAv120(boolean isAboveAv120) {
		this.isAboveAv120 = isAboveAv120;
	}
	public Integer getGoodTimes() {
		return goodTimes;
	}
	public void setGoodTimes(Integer goodTimes) {
		this.goodTimes = goodTimes;
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
	public String getGoodPeriod() {
		return goodPeriod;
	}
	public void setGoodPeriod(String goodPeriod) {
		this.goodPeriod = goodPeriod;
	}
	public Integer getLastPeriod() {
		return lastPeriod;
	}
	public void setLastPeriod(Integer lastPeriod) {
		this.lastPeriod = lastPeriod;
	}
	public Integer getUpProbability() {
		return upProbability;
	}
	public void setUpProbability(Integer upProbability) {
		this.upProbability = upProbability;
	}

	
}
