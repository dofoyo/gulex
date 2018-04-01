package com.rhb.gulex.reportdate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.reportdate.repository.ReportDateRepository;
import com.rhb.gulex.reportdate.spider.DownloadReportDate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DownloadReportDateTest {
	@Autowired
	DownloadReportDate downloadReportDate;
	
	@Autowired
	ReportDateRepository reportDateRepository;
	
	//@Test
	public void testGetReportDate(){
		long startTime = System.currentTimeMillis(); // 获取开始时间  
		//System.out.println("begin download and write.....");
		//downloadReportDate.go();
		//System.out.println("begin read.....");
		//reportDateRepository.init();
		
		String stockcode = "300022";
		Integer year = 2017;
		System.out.println(stockcode + " " + year + "年报日期 " + reportDateRepository.getReportDate(stockcode, year));

		
		Map<Integer,String> yearDates = reportDateRepository.getReportDates(stockcode);
		for(Map.Entry<Integer, String> yearDate : yearDates.entrySet()) {
			System.out.println(stockcode + ", " + yearDate.getKey() + ", " + yearDate.getValue());
		}
		
		
		long endTime = System.currentTimeMillis(); // 获取结束时间  
		
	    System.out.println("程序运行时间： " + (endTime - startTime) + "ms");  
		System.out.println("end.......");
	}
	
	//@Test
	public void testSaveReportDates() {
		String stockcode = "300022";
		Integer year = 2017;
		String date = "2018-03-31";
		Map<String,String> codeDates = new HashMap<String,String>();
		codeDates.put(stockcode, date);
		reportDateRepository.saveReportDates(codeDates, year);
	
		Map<Integer,String> yearDates = reportDateRepository.getReportDates(stockcode);
		for(Map.Entry<Integer, String> yearDate : yearDates.entrySet()) {
			System.out.println(stockcode + ", " + yearDate.getKey() + ", " + yearDate.getValue());
		}
		
		//reportDateRepository.saveReportDates(downloadCodes, year);
	}
	
}
