package com.rhb.gulex.pb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.GetMapping;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.pb.api.Pb;
import com.rhb.gulex.pb.service.PbService;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.service.TradeRecordDTO;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestController {
	@Autowired
	@Qualifier("PbServiceImp")
	PbService pbService;
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;	
	
	
	@Test
	public void test(){
		
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
			if(tre != null) {
				p.setSzzs(tre.getPrice().toString());
			}
			
			System.out.println(p);
		}
		
		
		
	}
}
