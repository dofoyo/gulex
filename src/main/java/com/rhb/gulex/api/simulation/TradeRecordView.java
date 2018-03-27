package com.rhb.gulex.api.simulation;

import java.math.BigDecimal;

public class TradeRecordView {
	private String date;
	private String buyorsell;
	private String stockname;
	private BigDecimal price;
	private Integer quantity;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBuyorsell() {
		return buyorsell;
	}
	public void setBuyorsell(String buyorsell) {
		this.buyorsell = buyorsell;
	}
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
