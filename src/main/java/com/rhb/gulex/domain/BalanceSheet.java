package com.rhb.gulex.domain;


public class BalanceSheet{
	private String period = "";
	private Double cash = 0.0;       			//�ʲ���ծ��.�����ʽ�
	private Double inventories = 0.0;  			//�ʲ���ծ��.�������
	private Double accountsReceivable = 0.0; 	//�ʲ���ծ��.Ӧ���˿��
	private Double notesReceivable = 0.0;  		//�ʲ���ծ��.Ӧ��Ʊ��
	private Double payables = 0.0;      		//�ʲ���ծ��.Ԥ���ʿ�
	private Double debt = 0.0; //�ʲ���ծ��.��ծ�ϼ�
	private Double assets = 0.0; // //�ʲ���ծ��.�ʲ��ܼ�
	

	public Double getDAR(){
		return this.debt/this.assets;
	}
	
	public Double getDebt() {
		return debt;
	}

	public void setDebt(Double debt) {
		this.debt = debt;
	}

	public Double getAssets() {
		return assets;
	}

	public void setAssets(Double assets) {
		this.assets = assets;
	}

	@Override
	public String toString() {
		return "BalanceSheet [period=" + period + ", cash=" + cash/100000000
				+ ", inventories=" + inventories/100000000 + ", accountsReceivable="
				+ accountsReceivable/100000000 + ", notesReceivable=" + notesReceivable/100000000
				+ ", payables=" + payables/100000000 + ", debt=" + debt/100000000 + ", assets="
				+ assets/100000000 + "]";
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public Double getInventories() {
		return inventories;
	}
	public void setInventories(Double inventories) {
		this.inventories = inventories;
	}

	public Double getAccountsReceivable() {
		return accountsReceivable;
	}

	public void setAccountsReceivable(Double accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	public Double getNotesReceivable() {
		return notesReceivable;
	}
	public void setNotesReceivable(Double notesReceivable) {
		this.notesReceivable = notesReceivable;
	}

	public Double getPayables() {
		return payables;
	}
	public void setPayables(Double payables) {
		this.payables = payables;
	}
	
}
