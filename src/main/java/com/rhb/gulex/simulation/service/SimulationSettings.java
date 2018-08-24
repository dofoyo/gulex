package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimulationSettings {
	
	//（买入）阈值设置：默认50。可设置为动态，根据前几次买入的盈亏决定。前几次买入赢率大，阀值降低些，否则，阀值升高
	private Integer buyValve = 50;   
	private boolean autoValveByPb = true;  //经过测试，根据PB进行阀值控制，在收益率影响不大的情况下，可以有效的控制仓位，即控制风险
	private boolean autoBuyValveByWinLossRatio = false;
	private Integer buyValvePeriodOfWinLossRatio = 5;
	private Integer buyLine = 120; //股价再250均线上，才可买入
	private Integer noBuyDays = 300; //新股禁入期间，再此期间内不买入。  300表示上市1年后，该股票原始股的中小股东的解禁后才能购买

	//（买入）融资设置：
	private boolean financing = true;    //******************
	
	//（买入）金额设置：
	private boolean amountFix = false; //true表示固定值，false表示固定比率
	private Integer fixAmount  = 50000;  //表示5万
	private Integer fixRate = 30;  //表示百分之几   ******************

	//（买入）加仓设置：
	private boolean addMore = true;
	private Integer addMoreThan = 10; //最近一次买入上涨10%以上，再次出现买点，可以加仓

	//（卖出）止盈设置
	private Integer sellLine = 60; //落选后，股价跌破60日均线，即卖出；可选项为20，30，60，120
	
	//（卖出）止损设置：
	private boolean stopLoss = false;  //止损操作,经过测试，止损不是好策略
	private Integer stopLossRate = -15; //买入后低于此比率，止损。

	//（卖出）持仓数量设置：
	private boolean onHandsLimit = false;
	private Integer onHandsLimitNumber = 20; //持仓数量为20， 若超出的，卖出最差的
	
	
	private LocalDate beginDate = LocalDate.parse("2010-01-01");  //能找到的年报发布日期是从2010年开始的
	private BigDecimal cash = new BigDecimal(1000000); //初始现金,默认为1百万。因为透支比不透效果更佳，所以系统选择透支。在透支的情况下，初始值为多少无影响
	
	public Integer getBuyValve(Integer winLossRatio) {
		if(autoBuyValveByWinLossRatio && winLossRatio!=null) {
			this.buyValve = winLossRatio>60 ? this.buyValve-5 : this.buyValve+5 ;
		}
		
		this.buyValve = this.buyValve>85? 85 : (this.buyValve<50 ? 50 : this.buyValve);
		
		return this.buyValve;
	}
	
	
	
	
	public boolean isAutoValveByPb() {
		return autoValveByPb;
	}

	public void setAutoValveByPb(boolean autoValveByPb) {
		this.autoValveByPb = autoValveByPb;
	}
	
	public Integer getBuyValve() {
		return buyValve;
	}
	public void setBuyValve(Integer buyValve) {
		this.buyValve = buyValve;
	}
	public Integer getBuyValvePeriod() {
		return buyValvePeriodOfWinLossRatio;
	}
	public void setBuyValvePeriod(Integer buyValvePeriod) {
		this.buyValvePeriodOfWinLossRatio = buyValvePeriod;
	}
	public boolean isAutoBuyValve() {
		return autoBuyValveByWinLossRatio;
	}
	public void setAutoBuyValve(boolean autoBuyValve) {
		this.autoBuyValveByWinLossRatio = autoBuyValve;
	}
	public Integer getBuyLine() {
		return buyLine;
	}
	public void setBuyLine(Integer buyLine) {
		this.buyLine = buyLine;
	}
	public Integer getNoBuyDays() {
		return noBuyDays;
	}
	public void setNoBuyDays(Integer noBuyDays) {
		this.noBuyDays = noBuyDays;
	}
	public boolean isFinancing() {
		return financing;
	}
	public void setFinancing(boolean financing) {
		this.financing = financing;
	}
	public boolean isAmountFix() {
		return amountFix;
	}
	public void setAmountFix(boolean amountFix) {
		this.amountFix = amountFix;
	}
	public Integer getFixAmount() {
		return fixAmount;
	}
	public void setFixAmount(Integer fixAmount) {
		this.fixAmount = fixAmount;
	}
	public Integer getFixRate() {
		return fixRate;
	}
	public void setFixRate(Integer fixRate) {
		this.fixRate = fixRate;
	}
	public boolean isAddMore() {
		return addMore;
	}
	public void setAddMore(boolean addMore) {
		this.addMore = addMore;
	}
	public Integer getAddMoreThan() {
		return addMoreThan;
	}
	public void setAddMoreThan(Integer addMoreThan) {
		this.addMoreThan = addMoreThan;
	}
	public Integer getSellLine() {
		return sellLine;
	}
	public void setSellLine(Integer sellLine) {
		this.sellLine = sellLine;
	}
	public boolean isStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(boolean stopLoss) {
		this.stopLoss = stopLoss;
	}
	public Integer getStopLossRate() {
		return stopLossRate;
	}
	public void setStopLossRate(Integer stopLossRate) {
		this.stopLossRate = stopLossRate;
	}
	public boolean isOnHandsLimit() {
		return onHandsLimit;
	}
	public void setOnHandsLimit(boolean onHandsLimit) {
		this.onHandsLimit = onHandsLimit;
	}
	public Integer getOnHandsLimitNumber() {
		return onHandsLimitNumber;
	}
	public void setOnHandsLimitNumber(Integer onHandsLimitNumber) {
		this.onHandsLimitNumber = onHandsLimitNumber;
	}
	public LocalDate getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	
	@Override
	public String toString() {
		return "SimulationSettings [buyValve=" + buyValve + ", buyValvePeriod=" + buyValvePeriodOfWinLossRatio + ", autoBuyValve="
				+ autoBuyValveByWinLossRatio + ", buyLine=" + buyLine + ", noBuyDays=" + noBuyDays + ", financing=" + financing
				+ ", amountFix=" + amountFix + ", fixAmount=" + fixAmount + ", fixRate=" + fixRate + ", addMore="
				+ addMore + ", addMoreThan=" + addMoreThan + ", sellLine=" + sellLine + ", stopLoss=" + stopLoss
				+ ", stopLossRate=" + stopLossRate + ", onHandsLimit=" + onHandsLimit + ", onHandsLimitNumber="
				+ onHandsLimitNumber + ", beginDate=" + beginDate + ", cash=" + cash + "]";
	}

	
	
	
}
