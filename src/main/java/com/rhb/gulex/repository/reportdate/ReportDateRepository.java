package com.rhb.gulex.repository.reportdate;

import java.util.Map;

public interface ReportDateRepository {

	public void init();
	public String getReportDate(String stockcode, Integer year);
	public Map<Integer,String>getReportDates(String stockcode);
}
