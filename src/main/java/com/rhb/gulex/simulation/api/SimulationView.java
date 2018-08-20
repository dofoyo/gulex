package com.rhb.gulex.simulation.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimulationView {
	private LocalDate date;
	private String stockcode;
	private String stockname;
	private String buyorsell;
	private BigDecimal price;
	private Integer quantity;
	private String note;
	
	
	public LocalDate getDate() {
		return date;
	}
	public String getDateString() {
		return date.toString();
	}
	public void setDate(LocalDate date) {
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
