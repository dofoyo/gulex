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
	
	//��������	���������������������������ʴ���20%(������������44%)
	//����		�����������������������ʴ���20%
	//�ֽ���		��Ӫ��ֽ���Ϊ�����ҳ�������������������ʴ���20%
	//Ӧ���˿�		Ӧ���˿��������С�����������������
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
	
	//�ʲ���ծ��  debt to assets ratio
	public Double getDAR(int year){
		fs.setYear(year);
		return fs.getDAR();
	}
	
	//�����ֽ���  cashflow to profit ratio
	public Double getCPR(int year){
		fs.setYear(year);
		return fs.getCPR();
	}
	
	//Ӧ��ռ�� Receivable Ratio
	public double getReceivableRatio(int year){
		fs.setYear(year);
		return fs.getReceivableRatio();
		
	}
	
}
