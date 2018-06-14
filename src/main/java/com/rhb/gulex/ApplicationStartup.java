package com.rhb.gulex;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.reportdate.repository.ReportDateRepository;
import com.rhb.gulex.simulation.service.SimulationService;
import com.rhb.gulex.stock.service.StockService;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ReportDateRepository rdr = event.getApplicationContext().getBean(ReportDateRepository.class);
		
		StockService ss = event.getApplicationContext().getBean(StockService.class);
		
		BluechipService bs =  event.getApplicationContext().getBean(BluechipService.class);
		
		SimulationService simu =  event.getApplicationContext().getBean(SimulationService.class);
	
		rdr.init();
		ss.init();
		bs.init();
		simu.init();

	
	}

}
