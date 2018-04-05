package com.rhb.gulex.simulation.api;

import java.math.BigDecimal;

public class TradeRecordViewPlus {
	private String stockcode;
	private String stockname;
	private Integer quantity;
	private String buyDate;
	private BigDecimal buyPrice;
	private String sellDate; 
	private BigDecimal sellPrice;
	
	
	
	public String getStockcode() {
		return stockcode;
	}

	public void setStockcode(String stockcode) {
		this.stockcode = stockcode;
	}

	public BigDecimal getBuyTotal() {
		return buyPrice.multiply(new BigDecimal(quantity));
	}
	
	public BigDecimal getSellTotal() {
		return sellPrice.multiply(new BigDecimal(quantity));
	}
	
	public BigDecimal getProfit() {
		return getSellTotal().subtract(getBuyTotal());
	}
	
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getSellDate() {
		return sellDate;
	}
	public void setSellDate(String sellDate) {
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
		return "TradeRecordViewPlus [stockname=" + stockname + ", quantity=" + quantity + ", buyDate=" + buyDate
				+ ", buyPrice=" + buyPrice + ", sellDate=" + sellDate + ", sellPrice=" + sellPrice + "]";
	}
	

	
}
