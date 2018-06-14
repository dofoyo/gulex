package com.rhb.gulex.pb.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.pb.service.PbService;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.service.TradeRecordDTO;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RestController
public class PbController {
	@Autowired
	@Qualifier("PbServiceImp")
	PbService pbService;
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;	
	
	@GetMapping("/pb")
	public ResponseContent<List<Pb>> getDzhs(){
		
		List<Pb> pbs = new ArrayList<Pb>();
		
		Pb pb;
		List<Map<String, String>> list = pbService.getHsagPbs();
		for(Map<String,String> m : list) {
			pb = new Pb();
			pb.setDate(m.get("date"));
			pb.setPb(m.get("hsag"));
			pbs.add(pb);
		}
		
				
		TradeRecordDTO tr = tradeRecordService.getTradeRecordsDTO("sh000001");
		TradeRecordEntity tre;
		for(Pb p : pbs) {
			tre = tr.getTradeRecordEntity(LocalDate.parse(p.getDate()));
			p.setSzzs(tre.getPrice().toString());
		}
		
		return new ResponseContent<List<Pb>>(ResponseEnum.SUCCESS, pbs);
	}
}
