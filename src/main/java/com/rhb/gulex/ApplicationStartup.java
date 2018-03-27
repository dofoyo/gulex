package com.rhb.gulex;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.rhb.gulex.service.stock.StockService;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		StockService ss = event.getApplicationContext().getBean(StockService.class);
		ss.init();
	}

}
