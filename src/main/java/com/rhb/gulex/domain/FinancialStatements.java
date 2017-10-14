package com.rhb.gulex.domain;

import java.util.Map;

import com.rhb.gulex.parse.ParseFinanceStatements;
import com.rhb.gulex.parse.ParseFinanceStatementsFromSina;

public class FinancialStatements {

	private String stockid;
	private Map balancesheets;
	private Map cashflows;
	private Map profitstatements;
	private String[] periods={"20151231","20141231","20131231"};
	
	
	private ParseFinanceStatements pfs = new ParseFinanceStatementsFromSina();
	
	public FinancialStatements(String stockid){
		this.stockid = stockid;
		this.balancesheets = pfs.parseBalanceSheet(stockid);
		this.cashflows = pfs.parseCashFlow(stockid);
		this.profitstatements = pfs.parseProfitStatement(stockid);
		
	}
	
	public boolean exists(String period){
		boolean flag = false;
		if(this.balancesheets.containsKey(period)
			&& this.cashflows.containsKey(period)
			&& this.profitstatements.containsKey(period)){
			flag = true;
		}
		return flag;
	}
	
	public void setYear(int year){
		periods[0] = Integer.toString(year) + "1231";
		periods[1] = Integer.toString(year-1) + "1231";
		periods[2] = Integer.toString(year-2) + "1231";
		
	}
	
	public String getCashflowString(){
		StringBuffer sb = new StringBuffer();
		if(this.cashflows.containsKey(periods[0])
				&& this.cashflows.containsKey(periods[1])
				&& this.cashflows.containsKey(periods[2])){

			sb.append(((CashFlow)this.cashflows.get(periods[0])).toString());
			sb.append("\n");
			sb.append(((CashFlow)this.cashflows.get(periods[1])).toString());
			sb.append("\n");
			sb.append(((CashFlow)this.cashflows.get(periods[2])).toString());
			
		}
		return sb.toString();
	}
	
	public Double getRateOfCashflow(){
		Double rate = 0.0;
		if(this.cashflows.containsKey(periods[0])
				&& this.cashflows.containsKey(periods[1])
				&& this.cashflows.containsKey(periods[2])){

			double or1 = ((CashFlow)this.cashflows.get(periods[0])).getNetCashFlow();
			double or2 = ((CashFlow)this.cashflows.get(periods[1])).getNetCashFlow();
			double or3 = ((CashFlow)this.cashflows.get(periods[2])).getNetCashFlow();
			
			if(or1>or2 && or2>or3 && or3>0){
				rate = (or1-or3)/or3;
			}
			
		}
			
		return rate;
	}
	
	public String getBalanceSheetString(){
		StringBuffer sb = new StringBuffer();
		if(this.balancesheets.containsKey(periods[0])
				&& this.balancesheets.containsKey(periods[1])
				&& this.balancesheets.containsKey(periods[2])){

			sb.append(((BalanceSheet)this.balancesheets.get(periods[0])).toString());
			sb.append("\n");
			sb.append(((BalanceSheet)this.balancesheets.get(periods[1])).toString());
			sb.append("\n");
			sb.append(((BalanceSheet)this.balancesheets.get(periods[2])).toString());
			
		}
		return sb.toString();
	}
	
	public Double getRateOfAccountsReceivable(){
		Double rate = 0.0;
		if(this.balancesheets.containsKey(periods[0])
				&& this.balancesheets.containsKey(periods[1])
				&& this.balancesheets.containsKey(periods[2])){

			double or1 = ((BalanceSheet)this.balancesheets.get(periods[0])).getAccountsReceivable();
			double or2 = ((BalanceSheet)this.balancesheets.get(periods[1])).getAccountsReceivable();
			double or3 = ((BalanceSheet)this.balancesheets.get(periods[2])).getAccountsReceivable();
			
			rate = (or1-or3)/or3;
		}
		return rate;
	}
	
	public String getProfitStatementString(){
		StringBuffer sb = new StringBuffer();
		if(this.profitstatements.containsKey(periods[0])
				&& this.profitstatements.containsKey(periods[1])
				&& this.profitstatements.containsKey(periods[2])){

			sb.append(((ProfitStatement)this.profitstatements.get(periods[0])).toString());
			sb.append("\n");
			sb.append(((ProfitStatement)this.profitstatements.get(periods[1])).toString());
			sb.append("\n");
			sb.append(((ProfitStatement)this.profitstatements.get(periods[2])).toString());
			
		}
		return sb.toString();
	}
		
	public Double getRateOfProfit(){
		Double rate = 0.0;
		if(this.profitstatements.containsKey(periods[0])
				&& this.profitstatements.containsKey(periods[1])
				&& this.profitstatements.containsKey(periods[2])){

		
			double or1 = ((ProfitStatement)this.profitstatements.get(periods[0])).getProfit();
			double or2 = ((ProfitStatement)this.profitstatements.get(periods[1])).getProfit();
			double or3 = ((ProfitStatement)this.profitstatements.get(periods[2])).getProfit();
			
			if(or1>or2 && or2>or3 && or3>0){
				rate = (or1-or3)/or3;
			}
		}
		return rate;
	}
	
	public Double getRateOfOperatngRevenue(){
		Double rate = 0.0;
		if(this.profitstatements.containsKey(periods[0])
				&& this.profitstatements.containsKey(periods[1])
				&& this.profitstatements.containsKey(periods[2])){
			double or1 = ((ProfitStatement)this.profitstatements.get(periods[0])).getOperatingRevenue();
			double or2 = ((ProfitStatement)this.profitstatements.get(periods[1])).getOperatingRevenue();
			double or3 = ((ProfitStatement)this.profitstatements.get(periods[2])).getOperatingRevenue();
			
			if(or1>or2 && or2>or3 && or3>0){
				rate = (or1-or3)/or3;
			}
		}
		return rate;
	}
	
	public Double getTurnoverRatioOfReceivable(int year){
		String period = Integer.toString(year) + "1231";
		double d = 0.0;
		
		BalanceSheet bs = (BalanceSheet)this.balancesheets.get(period);
		ProfitStatement ps = (ProfitStatement)this.profitstatements.get(period);
		if(bs != null && ps != null){
			double accountsReceivable = bs.getAccountsReceivable();
			double operatingRevenue = ps.getOperatingRevenue();
			d = operatingRevenue/accountsReceivable;
		}
		
		return d;
	}
	
	//资产负债率  debt to assets ratio
	public Double getDAR(){
		return ((BalanceSheet)this.balancesheets.get(periods[0])).getDAR();
	}
	
	//利润现金含量  cashflow to profit ratio
	public Double getCPR(){
		Double cash = ((CashFlow)this.cashflows.get(periods[0])).getNetCashFlow();
		Double profit = ((ProfitStatement)this.profitstatements.get(periods[0])).getProfit();
		return profit.intValue()==0 ? 0.0 : cash/profit;
	}
	
	//应收占比 Receivable Ratio
	public Double getReceivableRatio(){
		BalanceSheet bs = (BalanceSheet)this.balancesheets.get(periods[0]);
		ProfitStatement ps = (ProfitStatement)this.profitstatements.get(periods[0]);
		
		Double operating = ps.getOperatingRevenue();
		Double receivable = bs.getAccountsReceivable();
		
		return operating.intValue()==0 ? 0.0 : receivable/operating;
		
	}
	

}
