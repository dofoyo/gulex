package com.rhb.gulex.util;

public class LineChartDTO {
	private String lineName;
	private String pointName;
	private double value;
	
	public LineChartDTO(double value, String lineName, String pointName) {
		this.value = value;
		this.lineName = lineName;
		this.pointName = pointName;
	}
	
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	

}
