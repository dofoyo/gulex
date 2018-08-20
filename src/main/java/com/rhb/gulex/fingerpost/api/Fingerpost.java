package com.rhb.gulex.fingerpost.api;

import java.time.LocalDate;

public class Fingerpost {
	private String code;
	private String name;
	private LocalDate date;
	private String buysell;
	private String descript;

	public Fingerpost(String code, String name, LocalDate date,String buysell, String descript) {
		this.code = code;
		this.name = name;
		this.date = date;
		this.buysell = buysell;
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

	public String getBuysell() {
		return buysell;
	}

	public void setBuysell(String buysell) {
		this.buysell = buysell;
	}

	@Override
	public String toString() {
		return "Fingerpost [code=" + code + ", name=" + name + ", date=" + date + ", buysell=" + buysell + ", descript="
				+ descript + "]";
	}




}
