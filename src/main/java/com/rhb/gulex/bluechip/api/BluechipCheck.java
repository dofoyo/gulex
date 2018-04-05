package com.rhb.gulex.bluechip.api;

public class BluechipCheck {
	private String code;
	private String name;
	private String ipoDate;
	private String dhzDate;
	private String wyDate;
	private String reportDateError; // 主要找到有年报但没有年报发布日期的问题
	private String okYears;
	private String reportDate;
	

	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
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
	public String getIpoDate() {
		return ipoDate;
	}
	public void setIpoDate(String ipoDate) {
		this.ipoDate = ipoDate;
	}
	public String getDhzDate() {
		return dhzDate;
	}
	public void setDhzDate(String dhzDate) {
		this.dhzDate = dhzDate;
	}
	public String getWyDate() {
		return wyDate;
	}
	public void setWyDate(String wyDate) {
		this.wyDate = wyDate;
	}
	public String getReportDateError() {
		return reportDateError;
	}
	public void setReportDateError(String reportDateError) {
		this.reportDateError = reportDateError;
	}
	
	
	
	public String getOkYears() {
		return okYears;
	}
	public void setOkYears(String okYears) {
		this.okYears = okYears;
	}
	@Override
	public String toString() {
		return "BluechipCheck [code=" + code + ", name=" + name + ", ipoDate=" + ipoDate + ", dhzDate=" + dhzDate
				+ ", wyDate=" + wyDate + ", reportDateError=" + reportDateError + ", okYears=" + okYears
				+ ", reportDate=" + reportDate + "]";
	}

	
	
	
}
