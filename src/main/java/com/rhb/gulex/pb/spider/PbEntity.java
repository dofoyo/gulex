package com.rhb.gulex.pb.spider;

public class PbEntity {
	private String date;
	private String shag;  //上海A股
	private String szag;  //深圳A股
	private String hsag;  //沪深A股
	private String sszb;  //深市主板
	private String zxb;  // 中小板
	private String cyb;  //创业板
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getShag() {
		return shag;
	}
	public void setShag(String shag) {
		this.shag = shag;
	}
	public String getSzag() {
		return szag;
	}
	public void setSzag(String szag) {
		this.szag = szag;
	}
	public String getHsag() {
		return hsag;
	}
	public void setHsag(String hsag) {
		this.hsag = hsag;
	}
	public String getSszb() {
		return sszb;
	}
	public void setSszb(String sszb) {
		this.sszb = sszb;
	}
	public String getZxb() {
		return zxb;
	}
	public void setZxb(String zxb) {
		this.zxb = zxb;
	}
	public String getCyb() {
		return cyb;
	}
	public void setCyb(String cyb) {
		this.cyb = cyb;
	}
	@Override
	public String toString() {
		return "{\"date\":\"" + date + "\", \"shag\":\"" + shag + "\", \"szag\":\"" + szag + "\", \"hsag\":\"" + hsag + "\", \"sszb\":\"" + sszb
				+ "\", \"zxb\":\"" + zxb + "\", \"cyb\":\"" + cyb + "\"}";
	}
	
	
	
	
}
