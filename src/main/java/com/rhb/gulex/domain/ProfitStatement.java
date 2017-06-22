package com.rhb.gulex.domain;

public class ProfitStatement {
	private String period = "";

	private double allOperatingRevenue = 0.0;  	//��Ӫҵ����
	private double operatingRevenue = 0.0;  	//��Ӫҵ������
	private double allOperatingCost = 0.0; 		//��Ӫҵ�ɱ�
	private double operatingCost = 0.0; 		//��Ӫҵ��ɱ�
	private double operatingExpense = 0.0;  	//�������
	private double salesExpense = 0.0; 			//���۷���
	private double financeExpense = 0.0; 		//�������
	private double tax = 0.0; 					//Ӫҵ˰�𼰸���
	
	public double getProfit(){
		return operatingRevenue - operatingCost - operatingExpense - salesExpense - financeExpense; 
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public double getAllOperatingRevenue() {
		return allOperatingRevenue;
	}
	public void setAllOperatingRevenue(double allOperatingRevenue) {
		this.allOperatingRevenue = allOperatingRevenue;
	}
	public double getOperatingRevenue() {
		return operatingRevenue;
	}
	public void setOperatingRevenue(double operatingRevenue) {
		this.operatingRevenue = operatingRevenue;
	}
	public double getAllOperatingCost() {
		return allOperatingCost;
	}
	public void setAllOperatingCost(double allOperatingCost) {
		this.allOperatingCost = allOperatingCost;
	}
	public double getOperatingCost() {
		return operatingCost;
	}
	public void setOperatingCost(double operatingCost) {
		this.operatingCost = operatingCost;
	}
	public double getOperatingExpense() {
		return operatingExpense;
	}
	public void setOperatingExpense(double operatingExpense) {
		this.operatingExpense = operatingExpense;
	}
	public double getSalesExpense() {
		return salesExpense;
	}
	public void setSalesExpense(double salesExpense) {
		this.salesExpense = salesExpense;
	}
	public double getFinanceExpense() {
		return financeExpense;
	}
	public void setFinanceExpense(double financeExpense) {
		this.financeExpense = financeExpense;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	@Override
	public String toString() {
		return "ProfitStatement [period=" + period + ", profit="+ this.getProfit()/100000000 +",allOperatingRevenue="
				+ allOperatingRevenue/100000000 + ", operatingRevenue="
				+ operatingRevenue/100000000 + ", allOperatingCost=" + allOperatingCost/100000000
				+ ", operatingCost=" + operatingCost/100000000 + ", operatingExpense="
				+ operatingExpense/100000000 + ", salesExpense=" + salesExpense/100000000
				+ ", financeExpense=" + financeExpense/100000000 + ", tax=" + tax + "]";
	}

}
