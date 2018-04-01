package com.rhb.gulex.simulation.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.simulation.service.SimulationService;

@RestController
public class SimulationController {

	@Autowired
	@Qualifier("SimulationServiceImp")
	SimulationService  simulationService;
	
	@GetMapping("/traderecords")
	public ResponseContent<List<TradeRecordView>> getTradeRecordViews(){
		List<TradeRecordView> tradeRecordViews = simulationService.getTradeRecordViews();
		Collections.sort(tradeRecordViews, new Comparator<TradeRecordView>(){

			@Override
			public int compare(TradeRecordView arg0, TradeRecordView arg1) {
				return arg1.getDate().compareTo(arg0.getDate());
			}
			
		});
		return new ResponseContent<List<TradeRecordView>>(ResponseEnum.SUCCESS, tradeRecordViews);
	}
	
	@GetMapping("/onhands")
	public ResponseContent<List<OnHandView>> getOnHands(){
		List<OnHandView> OnHandViews = simulationService.getOnHandViews();
		Collections.sort(OnHandViews, new Comparator<OnHandView>(){

			@Override
			public int compare(OnHandView arg0, OnHandView arg1) {
				return arg1.getProfit().compareTo(arg0.getProfit());
			}
			
		});
		
		
		return new ResponseContent<List<OnHandView>>(ResponseEnum.SUCCESS, OnHandViews);
	}
	
	@GetMapping("/values")
	public ResponseContent<List<ValueView>> getValues(){
		List<ValueView> valueViews = simulationService.getValueViews();
		
		return new ResponseContent<List<ValueView>>(ResponseEnum.SUCCESS, valueViews);
	}

	
}
