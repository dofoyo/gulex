package com.rhb.gulex.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.simulation.service.Simulator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestSimulator {
	@Autowired
	Simulator simulator;
	
	@Test
	public void test(){
		//Integer[] amounts = {30000,50000,80000,100000,120000,150000,180000,200000};
		Integer[] amounts = {50000};
		//Integer[] upProbabilities = {30,35,40,45,50,55,60,65,70,75,80,85,90,95};
		Integer[] upProbabilities = {60};
		LocalDate beginDate = LocalDate.parse("2010-01-01"); 
		boolean flag = true;
		
		BigDecimal profit;
		StringBuffer sb = new StringBuffer();
		for(Integer amount : amounts){
			for(Integer upProbability : upProbabilities){
				simulator.setBeginDate(beginDate);
				simulator.setAmount(new BigDecimal(amount));
				simulator.setUpProbability(upProbability);
				simulator.setOverdraft(flag);
				
				profit = simulator.simulate();
				sb.append("(" + amount.toString() + "," + upProbability.toString() + ") = " + profit.toString());
				sb.append("\n");
			}
		}
		
		System.out.println(sb.toString());
		
	}
	
	//@Test
	public void test1(){
		StringBuffer sb = new StringBuffer();
		Integer amount = 80000;
		Integer upProbability = 50;
		simulator.setAmount(new BigDecimal(amount));
		simulator.setUpProbability(upProbability);
		BigDecimal profit = simulator.simulate();
		sb.append("(" + amount.toString() + "," + upProbability.toString() + ") = " + profit.toString());
		sb.append("\n");

		System.out.println(sb.toString());
		
	}
}
