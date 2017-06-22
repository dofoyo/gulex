package com.rhb.gulex.domain;

public class Stock {

	private String stockid;
	private String stockname;
	private FinancialStatements fs;
	
	public Stock(String stockid, String stockname){
		this.stockid = stockid;
		this.stockname = stockname;
		this.fs = new FinancialStatements(stockid);
	}
	
	//销售收入	销售收入持续增长，且年均增长率大于20%(二年增长大于44%)
	//利润		利润持续增长，且年均增长率大于20%
	//现金流		经营活动现金流为正，且持续增长，且年均增长率大于20%
	//应收账款		应收账款的增长率小于销售收入的增长率
	public boolean isOk(int year){
		fs.setYear(year);
		boolean flag = false;
		if(fs.getRateOfOperatngRevenue()>0.44 
			&& fs.getRateOfProfit()>0.44
			&& fs.getRateOfCashflow()>0.44
			&& fs.getRateOfOperatngRevenue()>fs.getRateOfAccountsReceivable()
			){
			flag = true;
		}
		
		return flag;
	}
	
	public boolean exists(String period){
		return fs.exists(period);
	}
	
	public String getStockName(){
		return stockname;
	}
	
	public String getRateOfFinancialStatements(int year){
		fs.setYear(year);
		StringBuffer sb = new StringBuffer();
		sb.append(stockname);
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
	
}
