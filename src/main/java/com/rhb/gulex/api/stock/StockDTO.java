package com.rhb.gulex.api.stock;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockDTO {
	private String code;
	private String name;
	private LocalDate ipoDate;
	private String goodPeriod;
	private Integer goodTimes;
	private Integer lastPeriod;
	private Integer upProbability;
	private BigDecimal av120;
	private BigDecimal price;
	private LocalDate date;
	private BigDecimal increase;
	
	
	
	public BigDecimal getIncrease() {
		return increase;
	}

	public void setIncrease(BigDecimal increase) {
		this.increase = increase;
	}

	public LocalDate getIpoDate() {
		return ipoDate;
	}

	public void setIpoDate(LocalDate ipoDate) {
		this.ipoDate = ipoDate;
	}

	public boolean isPriceAboveAv120(){
		return price.compareTo(av120)==1;
	}
	
	public boolean shouldSell(){
		BigDecimal ratio = price.subtract(av120).divide(price,2,BigDecimal.ROUND_HALF_UP);
		return ratio.compareTo(new BigDecimal(-0.1))==-1 ? true : false;
	}
	
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


	public BigDecimal getAv120() {
		return av120;
	}

	public void setAv120(BigDecimal av120) {
		this.av120 = av120;
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
		
		return upProbability == null ? 0 : upProbability;
	}
	public void setUpProbability(Integer upProbability) {
		this.upProbability = upProbability;
	}

	@Override
	public String toString() {
		return "StockDTO [code=" + code + ", name=" + name + ", ipoDate=" + ipoDate + ", goodPeriod=" + goodPeriod
				+ ", goodTimes=" + goodTimes + ", lastPeriod=" + lastPeriod + ", upProbability=" + upProbability
				+ ", av120=" + av120 + ", price=" + price + ", date=" + date + "]";
	}


	
	
}
