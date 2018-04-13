package com.rhb.gulex.traderecord.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeRecordEntity {
	private String code;
	private LocalDate date;
	private BigDecimal price;
	private BigDecimal av120;
	private BigDecimal av60;
	private BigDecimal av30;
	private Integer aboveAv120Days;
	private BigDecimal midPrice;
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getAv30() {
		return av30;
	}
	public void setAv30(BigDecimal av30) {
		this.av30 = av30;
	}
	public BigDecimal getAv60() {
		return av60;
	}
	public void setAv60(BigDecimal av60) {
		this.av60 = av60;
	}
	public BigDecimal getMidPrice() {
		return midPrice;
	}
	public void setMidPrice(BigDecimal midPrice) {
		this.midPrice = midPrice;
	}
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
	
	public BigDecimal getAv120() {
		return av120;
	}
	public void setAv120(BigDecimal av120) {
		this.av120 = av120;
	}
	public Integer getAboveAv120Days() {
		return aboveAv120Days;
	}
	public void setAboveAv120Days(Integer aboveAv120Days) {
		this.aboveAv120Days = aboveAv120Days;
	}
	
	public boolean isPriceOnAv(Integer av) {
		boolean flag = false;
		switch(av) {
			case 30: flag = isPriceOnAv30(); break;
			case 60: flag =  isPriceOnAv60(); break;
			case 120: flag = isPriceOnAv120(); break;
			default: flag =  isPriceOnAv60();
		}
		
		return flag;
	}
	
	private boolean isPriceOnAv120(){
		return (price!=null && av120!=null && price.compareTo(av120)==-1) ? false : true;
	}
	
	private boolean isPriceOnAv60() {
		return (price!=null && av60!=null && price.compareTo(av60)==-1) ? false : true;
	}
	
	private boolean isPriceOnAv30() {
		return (price!=null && av30!=null && price.compareTo(av30)==-1) ? false : true;
	}
	
	public Integer getBiasOfAv120(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && av120!=null){
			//i = ((price.subtract(av120)).divide(av120,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			i = ((price.subtract(av120)).divide(av120,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue();

/*		if(i.intValue()>10 && i.intValue()<= 20){
			bias = i.multiply(new BigDecimal(1.5)).intValue();
		}else if(i.intValue()>20 && i.intValue()<=30){
			bias = i.multiply(new BigDecimal(2)).intValue();
		}else if(i.intValue()>30){
			bias = i.multiply(new BigDecimal(2.5)).intValue();
		}*/
		
		return bias;
	}
	
	
	public Integer getBiasOfMidPrice(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && midPrice!=null){
			//i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue()/2;  //经测试，取中位数的半值结果最好

/*		if(i.intValue()>10 && i.intValue()<= 20){
			bias = i.multiply(new BigDecimal(1.5)).intValue();
		}else if(i.intValue()>20 && i.intValue()<=30){
			bias = i.multiply(new BigDecimal(2)).intValue();
		}else if(i.intValue()>30){
			bias = i.multiply(new BigDecimal(2.5)).intValue();
		}*/
		
		return bias;
	}
	
	/*
	 *上涨概率
	 *最近100个交易日，股价大于120日均线的天数，为上涨概率，如设为a 
	 *股价偏离120日均线的百分比，每多1%， 减一个点，即：a - ((股价-av120)/av120 * 100)
	 *股价偏离价格中位数的百分比，每多1%，减一个点，即：a - ((股价-midPrce)/mdiPrice *100)
	 *
	 */
	
	public Integer getUpProbability(){
		Integer upProbability = this.aboveAv120Days;
		upProbability = upProbability - this.getBiasOfAv120() - this.getBiasOfMidPrice();
		return upProbability>0 ? upProbability : 0;
		
	}
	
	public String getUpProbabilityString() {
		return  "upProbability=" + this.aboveAv120Days.toString() +"-"+ this.getBiasOfAv120().toString() +"-"+ this.getBiasOfMidPrice().toString() + "=" + getUpProbability().toString();
	}
	@Override
	public String toString() {
		return "TradeRecordEntity [code=" + code + ", date=" + date + ", price=" + price + ", av120=" + av120
				+ ", av60=" + av60 + ", av30=" + av30 + ", aboveAv120Days=" + aboveAv120Days + ", midPrice=" + midPrice
				+ "]";
	}
	



}
