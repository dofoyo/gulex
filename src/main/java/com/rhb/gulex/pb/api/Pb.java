package com.rhb.gulex.pb.api;

public class Pb {
	private String date;
	private String szzs;  //上证指数
	private String pb;     //沪深A股平均市净率
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSzzs() {
		return szzs;
	}
	public void setSzzs(String szzs) {
		this.szzs = szzs;
	}
	public String getPb() {
		return pb;
	}
	public void setPb(String pb) {
		this.pb = pb;
	}
	@Override
	public String toString() {
		return "Pb [date=" + date + ", szzs=" + szzs + ", pb=" + pb + "]";
	}
	

}
