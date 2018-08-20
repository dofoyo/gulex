package com.rhb.gulex.fingerpost.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.simulation.api.SimulationView;
import com.rhb.gulex.simulation.service.SimulationService;
import com.rhb.gulex.traderecord.api.TradeRecordJdh;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RestController
public class FingerpostController {
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	@Autowired
	@Qualifier("SimulationServiceImp")
	SimulationService simulationService;
	

	
	@GetMapping("/fingerpost")
	public ResponseContent<List<Fingerpost>> getDzhs(
			@RequestParam(value="keyword", defaultValue="") String keyword){
		
		List<Fingerpost> fingerposts = new ArrayList<Fingerpost>();
		
		List<TradeRecordJdh> tradeRecordJdhs = tradeRecordService.getJdhs();

		List<SimulationView> tradeRecordViews = simulationService.getTradeRecordViews();
		
		for(TradeRecordJdh jdh : tradeRecordJdhs) {
			if(jdh.getCode().contains(keyword) || jdh.getName().contains(keyword)) {
				fingerposts.add(new Fingerpost(jdh.getCode(),jdh.getName(),jdh.getDate(),"买入",jdh.getDescript()));
			}
		}
		
		for(SimulationView view : tradeRecordViews) {
			if(view.getStockcode().contains(keyword) || view.getStockname().contains(keyword)) {
				fingerposts.add(new Fingerpost(view.getStockcode(),view.getStockname(),view.getDate(),view.getBuyorsell(),view.getNote()));
			}
			
		}
		
		
		Collections.sort(fingerposts, new Comparator<Fingerpost>(){

			@Override
			public int compare(Fingerpost arg0, Fingerpost arg1) {
				return arg1.getDate().compareTo(arg0.getDate());
			}
			
		});

		fingerposts = fingerposts.subList(0, fingerposts.size()>200 ? 200 : fingerposts.size());

		
		return new ResponseContent<List<Fingerpost>>(ResponseEnum.SUCCESS, fingerposts);
	}

}
