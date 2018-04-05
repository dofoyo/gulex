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
	}
	@Override
	public String toString() {
		return "TradeDetail [seriesid=" + seriesid + ", code=" + code + ", name=" + name + ", quantity=" + quantity
				+ ", buyDate=" + buyDate + ", buyCost=" + buyCost + ", sellDate=" + sellDate + ", sellPrice="
				+ sellPrice + "]";
	}
	
	
	
}
