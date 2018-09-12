package com.rhb.gulex.fingerpost.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Fingerpost {
	private String code;
	private String name;
	private LocalDate date;
	private String buysell;
	private String descript;
	private BigDecimal price;

	public Fingerpost(String code, String name, LocalDate date,String buysell, String descript,BigDecimal price) {
		this.code = code;
		this.name = name;
		this.date = date;
		this.buysell = buysell;
		this.descript = descript;
		this.price = price;
		
	}
	
	
	
	public BigDecimal getPrice() {
		return price;
	}



	public void setPrice(BigDecimal price) {
		this.price = price;
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
				+ descript + ", price=" + price + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buysell == null) ? 0 : buysell.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((descript == null) ? 0 : descript.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fingerpost other = (Fingerpost) obj;
		if (buysell == null) {
			if (other.buysell != null)
				return false;
		} else if (!buysell.equals(other.buysell))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (descript == null) {
			if (other.descript != null)
				return false;
		} else if (!descript.equals(other.descript))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

	
}
