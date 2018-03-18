package com.rhb.gulex.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeRecord {
	private LocalDate date;
	private BigDecimal price;
	private BigDecimal avarage;
	
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
	
	public BigDecimal getAvarage() {
		return avarage;
	}
	public void setAvarage(BigDecimal avarage) {
		this.avarage = avarage;
	}
	public boolean isPriceOnAvarage(){
		return (price!=null && avarage!=null && price.compareTo(avarage)==-1) ? false : true;
	}
	
	public Integer getBias(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && avarage!=null){
			i = ((price.subtract(avarage)).divide(avarage,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue();
		//System.out.print(bias);
		//System.out.print("==>");
		if(i.intValue()>10 && i.intValue()<= 20){
			bias = i.multiply(new BigDecimal(1.5)).intValue();
		}else if(i.intValue()>20 && i.intValue()<=30){
			bias = i.multiply(new BigDecimal(2)).intValue();
		}else if(i.intValue()>30){
			bias = i.multiply(new BigDecimal(2.5)).intValue();
		}
		//System.out.println(bias);
		
		return bias;
	}
	

}
