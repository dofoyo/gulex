package com.rhb.gulex.traderecord.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeRecordEntity {
	private String code;
	private LocalDate date;
	private BigDecimal price;
	private BigDecimal av120;
	private BigDecimal av60;
	private BigDecimal av250;
	private Integer aboveAv120Days;
	private Integer aboveAv60Days = 0;
	private Integer belowAv60Days = 0;
	private BigDecimal midPrice;
	private Integer buyDay = 0;
	
	
	
	public Integer getAboveAv60Days() {
		return aboveAv60Days;
	}
	public void setAboveAv60Days(Integer aboveAv60Days) {
		this.aboveAv60Days = aboveAv60Days;
	}
	public Integer getBelowAv60Days() {
		return belowAv60Days;
	}
	public void setBelowAv60Days(Integer belowAv60Days) {
		this.belowAv60Days = belowAv60Days;
	}
	public Integer getBuyDay() {
		return buyDay;
	}
	public void setBuyDay(Integer buyDay) {
		this.buyDay = buyDay;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getAv250() {
		return av250;
	}
	public void setAv250(BigDecimal av250) {
		this.av250 = av250;
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
	
	public Integer getRateOfPriceOn250() {
		return price.subtract(av250).divide(av250,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
	}
	
	
	public boolean isPriceOnAv(Integer av) {
		boolean flag = false;
		switch(av) {
			case 60: flag =  isPriceOnAv60(); break;
			case 120: flag = isPriceOnAv120(); break;
			case 250: flag = isPriceOnAv250(); break;
			default: flag =  isPriceOnAv60();
		}
		
		return flag;
	}

	public boolean is120On250() {
		return (av250!=null && av120!=null && av120.compareTo(av250)==1) ? true : false;
	}
	
	public boolean is60On120() {
		return (av60!=null && av120!=null && av60.compareTo(av120)==1) ? true : false;
	}
	
	private boolean isPriceOnAv120(){
		return (price!=null && av120!=null && price.compareTo(av120)==1) ? true : false;
	}
	
	private boolean isPriceOnAv60() {
		return (price!=null && av60!=null && price.compareTo(av60)==1) ? true : false;
	}
	
	private boolean isPriceOnAv250() {
		return (price!=null && av250!=null && price.compareTo(av250)==1) ? true : false;
	}
	
	public Integer getBiasOfAv120(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && av120!=null){
			i = ((price.subtract(av120)).divide(av120,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue();
		
		return bias;
	}
	
		
	public Integer getBiasOfMidPrice(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && midPrice!=null){
			//i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue()/2;  //经测试，取中位数的半值结果最好

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
		Integer upProbability = this.aboveAv60Days;
		upProbability = upProbability - this.getBiasOfAv120() - this.getBiasOfMidPrice();
		return upProbability>0 ? upProbability : 0;
		
	}
	@Override
	public String toString() {
		return "TradeRecordEntity [code=" + code + ", date=" + date + ", price=" + price + ", av120=" + av120
				+ ", av60=" + av60 + ", av250=" + av250 + ", aboveAv120Days=" + aboveAv120Days + ", aboveAv60Days="
				+ aboveAv60Days + ", belowAv60Days=" + belowAv60Days + ", midPrice=" + midPrice + ", buyDay=" + buyDay
				+ "]";
	}
	

	



}
