package com.rhb.gulex.financialstatements;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.financialstatement.service.FinancialStatementService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FinancialStatementServiceTest {
	
	@Autowired
	FinancialStatementService financialStatementService;
	
	@Test
	public void test(){
		System.out.println("beging.....");
		
		String stockcode="601199";
		Integer year = 2009;
				
		boolean isok = financialStatementService.isOk(stockcode, year);
		
		System.out.println(isok);
		
		System.out.println("..... end");
		
	}
}
