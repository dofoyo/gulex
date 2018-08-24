package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeDetail {
	private String seriesid;
	private String code;
	private String name;
	private Integer quantity;
	private LocalDate buyDate;
	private BigDecimal buyCost;
	private LocalDate sellDate;
	private BigDecimal sellPrice;
	
	private BigDecimal lowestPrice = new BigDecimal(1000);
	private BigDecimal highestPrice = new BigDecimal(0);
	
	private String buynote = "";
	private String sellnote = "";
	
	

	public String getBuynote() {
		return buynote;
	}
	public void setBuynote(String buynote) {
		this.buynote = buynote;
	}
	public String getSellnote() {
		return sellnote;
	}
	public void setSellnote(String sellnote) {
		this.sellnote = sellnote;
	}
	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	public BigDecimal getHighestPrice() {
		return highestPrice;
	}
	public void setHighestPrice(BigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}
	
	public Integer getProfitRate() {
		Integer rate = sellPrice.subtract(buyCost).divide(buyCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		return rate;
	}
	
	public Integer getLowestRate() {
		Integer rate = lowestPrice.subtract(buyCost).divide(buyCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();

		return rate;
	}

	public Integer getHighestRate() {
		Integer rate = highestPrice.subtract(buyCost).divide(buyCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();

		return rate;
	}

	public String getSeriesid() {
		return seriesid;
	}
	public void setSeriesid(String seriesid) {
		this.seriesid = seriesid;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public LocalDate getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(LocalDate buyDate) {
		this.buyDate = buyDate;
	}
	public BigDecimal getBuyCost() {
		return buyCost;
	}
	public void setBuyCost(BigDecimal buyCost) {
		this.buyCost = buyCost;
	}
	public LocalDate getSellDate() {
		return sellDate;
	}
	public void setSellDate(LocalDate sellDate) {
		this.sellDate = sellDate;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;

		if(sellPrice.compareTo(lowestPrice) == -1) {
			lowestPrice = sellPrice;
		}
		
		if(sellPrice.compareTo(highestPrice) == 1) {
			highestPrice = sellPrice;
		}
		
	}
	@Override
	public String toString() {
		return "TradeDetail [seriesid=" + seriesid + ", code=" + code + ", name=" + name + ", quantity=" + quantity
				+ ", buyDate=" + buyDate + ", buyCost=" + buyCost + ", sellDate=" + sellDate + ", sellPrice="
				+ sellPrice + ", lowestPrice=" + lowestPrice + ", highestPrice=" + highestPrice + ", buynote=" + buynote + ", sellnote=" + sellnote
				+ "]";
	}

	
	
	
	
}
