package com.rhb.gulex.repository;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.repository.reportdate.DownloadReportDate;
import com.rhb.gulex.repository.reportdate.ReportDateRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DownloadReportDateTest {
	@Autowired
	DownloadReportDate downloadReportDate;
	
	@Autowired
	ReportDateRepository reportDateRepository;
	
	@Test
	public void test(){
		System.out.println("begin download and write.....");
		//downloadReportDate.go();
		System.out.println("begin read.....");
		reportDateRepository.init();
		
		long startTime = System.currentTimeMillis(); // 获取开始时间  
		LocalDate date = LocalDate.parse(reportDateRepository.getReportDate("300536", 2017));
		long endTime = System.currentTimeMillis(); // 获取结束时间  
		System.out.println(date);
		
	    System.out.println("程序运行时间： " + (endTime - startTime) + "ms");  
		System.out.println("end.......");
	}
	
}
