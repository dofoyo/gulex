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
	
	@GetMapping("/traderecordplus")
	public ResponseContent<List<TradeRecordViewPlus>> getTradeRecordViewPlus(){
		List<TradeRecordViewPlus> tradeRecordViews = simulationService.getTradeRecordViewPlus();
		Collections.sort(tradeRecordViews, new Comparator<TradeRecordViewPlus>(){

			@Override
			public int compare(TradeRecordViewPlus arg0, TradeRecordViewPlus arg1) {
				return arg1.getProfit().compareTo(arg0.getProfit());
			}
			
		});
		return new ResponseContent<List<TradeRecordViewPlus>>(ResponseEnum.SUCCESS, tradeRecordViews);
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
	
	@GetMapping("/dayvalues")
	public ResponseContent<List<ValueView>> getDayValues(){
		List<ValueView> valueViews = simulationService.getDayValueViews();
		
		Collections.sort(valueViews, new Comparator<ValueView>() {

			@Override
			public int compare(ValueView o1, ValueView o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
			
		});
		
		return new ResponseContent<List<ValueView>>(ResponseEnum.SUCCESS, valueViews);
	}

	@GetMapping("/yearvalues")
	public ResponseContent<List<ValueView>> getYearValues(){
		List<ValueView> valueViews = simulationService.getYearValueViews();
		
		Collections.sort(valueViews, new Comparator<ValueView>() {

			@Override
			public int compare(ValueView o1, ValueView o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
			
		});
		
		return new ResponseContent<List<ValueView>>(ResponseEnum.SUCCESS, valueViews);
	}
	
}
