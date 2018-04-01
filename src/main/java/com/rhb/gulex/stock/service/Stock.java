package com.rhb.gulex.stock.service;

import java.time.LocalDate;


public class Stock {

	private String stockId;
	private String stockName;
	private LocalDate ipoDate;
	private Integer deleted = 0;
	
	public LocalDate getIpoDate() {
		return ipoDate;
	}

	public void setIpoDate(LocalDate ipoDate) {
		this.ipoDate = ipoDate;
	}


	public Stock(String stockid, String stockname){
		this.stockId = stockid;
		this.stockName = stockname;
	}
	
	public String getStockName(){
		return stockName;
	}

	public String getStockId() {
		return stockId;
	}
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
}
