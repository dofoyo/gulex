package com.rhb.gulex.simulation;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.simulation.api.TradeRecordViewPlus;
import com.rhb.gulex.simulation.service.SimulationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestSimulationService {

	@Autowired
	SimulationService simulationService;
	
	@Test
	public void test() {
		List<TradeRecordViewPlus> list = simulationService.getTradeRecordViewPlus();
		for(TradeRecordViewPlus view : list) {
			//System.out.println(view);
		}
	}
}
