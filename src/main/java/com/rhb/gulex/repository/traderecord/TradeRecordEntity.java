package com.rhb.gulex.repository.traderecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeRecordEntity {
	private LocalDate date;
	private BigDecimal price;
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	

}
