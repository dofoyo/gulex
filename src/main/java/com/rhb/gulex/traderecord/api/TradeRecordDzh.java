package com.rhb.gulex.traderecord.api;

public class TradeRecordDzh {
	private String code;
	private String name;
	private String dzhDate;
	private String okYears;
	private String ipoDate;
	
	
	public String getIpoDate() {
		return ipoDate;
	}
	public void setIpoDate(String ipoDate) {
		this.ipoDate = ipoDate;
	}
	public String getOkYears() {
		return okYears;
	}
	public void setOkYears(String okYears) {
		this.okYears = okYears;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDzhDate() {
		return dzhDate;
	}
	public void setDzhDate(String dzhDate) {
		this.dzhDate = dzhDate;
	}
	@Override
	public String toString() {
		return "TradeRecordDzh [code=" + code + ", name=" + name + ", dzhDate=" + dzhDate + ", okYears=" + okYears
				+ ", ipoDate=" + ipoDate + "]";
	}

}
