package com.rhb.gulex.simulation.api;

import java.math.BigDecimal;

public class OnHandView {
	private String tradeDate;
	private String code;
	private String name;
	private Integer quantity;
	private BigDecimal cost;
	private BigDecimal price;
	
	
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getValue(){
		return price.multiply(new BigDecimal(quantity));
	}
	public BigDecimal getProfit(){
		return price.subtract(cost).multiply(new BigDecimal(quantity));
	}
	
}
