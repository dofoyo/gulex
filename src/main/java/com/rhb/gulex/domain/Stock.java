package com.rhb.gulex.domain;

import java.util.HashMap;
import java.util.Map;

public class Stock {

	private String stockId;
	private String stockName;
	private Map<Integer,Integer> goodTims;
	private FinancialStatements fs;
	
	public Stock(String stockid, String stockname){
		this.stockId = stockid;
		this.stockName = stockname;
		this.fs = new FinancialStatements(stockid);
		this.goodTims = new HashMap<Integer,Integer>();
	}
	
	//销售收入	销售收入持续增长，且年均增长率大于20%(二年增长大于44%)
	//利润		利润持续增长，且年均增长率大于20%
	//现金流		经营活动现金流为正，且持续增长，且年均增长率大于20%
	//应收账款		应收账款的增长率小于销售收入的增长率
	//现金流与利润的比率大于1
	//应收占比销售额的比例小于20%
	private boolean isOK(int year){
		fs.setYear(year);
		boolean flag = false;
		if(fs.getRateOfOperatngRevenue()>0.44 //销售收入持续增长，且年均增长率大于20%(二年增长大于44%)
			&& fs.getRateOfProfit()>0.44  //利润持续增长，且年均增长率大于20%(二年增长大于44%)
			&& fs.getRateOfCashflow()>0.44  //经营活动现金流为正，且持续增长，且年均增长率大于20%(二年增长大于44%)
			//&& fs.getRateOfOperatngRevenue()>fs.getRateOfAccountsReceivable() //应收账款的增长率小于销售收入的增长率
			&& fs.getCPR()>=1  //现金流与利润的比率大于1
			&& fs.getReceivableRatio()<=0.2  //应收占比销售额的比例小于20%
			){
			flag = true;
		}
		
		return flag;
	}
	
	public void refresh(int bYear, int eYear){
		for(int year=bYear; year<=eYear; year++){
			if(this.isOK(year)){
				this.goodTims.put(year, 1);
			}
		}
	}
	
	public boolean isGood(int year){
		return goodTims.containsKey(year);
	}
	
	public int getGoodTimes(){
		return goodTims.size();
	}
	
	public boolean exists(String period){
		return fs.exists(period);
	}
	
	public String getStockName(){
		return stockName;
	}
	
	public String getRateOfFinancialStatements(int year){
		fs.setYear(year);
		StringBuffer sb = new StringBuffer();
		sb.append(stockName);
		sb.append(",");
		sb.append("RateOfOperatngRevenue = ");
		sb.append(fs.getRateOfOperatngRevenue());
		sb.append(",");
		sb.append("RateOfProfit = ");
		sb.append(fs.getRateOfProfit());
		sb.append(",");
		sb.append("RateOfCashflow = ");
		sb.append(fs.getRateOfCashflow());
		sb.append(",");
		sb.append("RateOfAccountsReceivable = ");
		sb.append(fs.getRateOfAccountsReceivable());
		
		return sb.toString();
	}
	
	public String getDetailOfFinancialStatements(int year){
		fs.setYear(year);
		StringBuffer sb = new StringBuffer();
		sb.append(fs.getBalanceSheetString());
		sb.append("\n");
		sb.append(fs.getProfitStatementString());
		sb.append("\n");
		sb.append(fs.getCashflowString());
		return sb.toString();

	}
	
	public Double getTurnoverRatioOfReceivable(int year){
		return fs.getTurnoverRatioOfReceivable(year);
	}
	
	//资产负债率  debt to assets ratio
	public Double getDAR(int year){
		fs.setYear(year);
		return fs.getDAR();
	}
	
	//利润现金含量  cashflow to profit ratio
	public Double getCPR(int year){
		fs.setYear(year);
		return fs.getCPR();
	}
	
	//应收占比 Receivable Ratio
	public double getReceivableRatio(int year){
		fs.setYear(year);
		return fs.getReceivableRatio();
		
	}

	public String getStockId() {
		return stockId;
	}
	
}
