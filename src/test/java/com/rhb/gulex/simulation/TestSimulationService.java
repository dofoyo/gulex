package com.rhb.gulex.simulation;

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
	
	@Test
	public void test() {
		List<SimulationViewPlus> list = simulationService.getTradeRecordViewPlus();
		for(SimulationViewPlus view : list) {
			//System.out.println(view);
		}
	}
}
