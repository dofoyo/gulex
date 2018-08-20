package com.rhb.gulex.traderecord.api;

import java.time.LocalDate;

public class TradeRecordJdh {
	private String code;
	private String name;
	private LocalDate date;
	private String descript;

	public TradeRecordJdh(String code, String name, LocalDate date, String descript) {
		this.code = code;
		this.name = name;
		this.date = date;
		this.descript = descript;
		
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
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getDateString() {
		return this.date.toString();
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	@Override
	public String toString() {
		return "TradeRecordJdh [code=" + code + ", name=" + name + ", date=" + date + ", descript=" + descript + "]";
	}



}
