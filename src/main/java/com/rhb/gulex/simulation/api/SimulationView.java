package com.rhb.gulex.simulation.api;

import java.math.BigDecimal;

public class SimulationView {
	private String date;
	private String stockcode;
	private String stockname;
	private String buyorsell;
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
	public String getStockcode() {
		return stockcode;
	}
	public void setStockcode(String stockcode) {
		this.stockcode = stockcode;
	}
	
	
}
