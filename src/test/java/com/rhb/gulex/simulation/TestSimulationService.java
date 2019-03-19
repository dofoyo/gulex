package com.rhb.gulex.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.simulation.api.SimulationViewPlus;
import com.rhb.gulex.simulation.service.SimulationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestSimulationService {

	@Autowired
	SimulationService simulationService;
	
	//@Test
	public void test() {
		List<SimulationViewPlus> list = simulationService.getTradeRecordViewPlus();
		for(SimulationViewPlus view : list) {
			//System.out.println(view);
		}
	}
	
	
	@Test
	public void test1(){
		BigDecimal total;
		StringBuffer sb = new StringBuffer();

		simulationService.setBeginDate(LocalDate.parse("2010-01-01"));
		simulationService.init();
		
		total = simulationService.getTotal();
		sb.append(simulationService.getSettingString());
		sb.append("\n");
		sb.append("total = " + total.toString());
		sb.append("\n");
				

		System.out.println(sb.toString());
	}
	
	/*
	 * noBuyDays, profit
	 *   365      5669722
	 *   330      5680534
	 *   300      5729778
	 *   250	  5517031
	 *   0        5203190
	 *   
	 *   5651064.00
	 *   
	 */
}
