package com.rhb.gulex.traderecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradeRecordRepositoryTest {
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepositoryImpFromDzh;

	@Autowired
	@Qualifier("TradeRecordRepositoryFromQt")
	TradeRecordRepository tradeRecordRepositoryFromQt;
	
	//@Test
	public void test(){
		List<TradeRecordEntity> records = tradeRecordRepositoryImpFromDzh.getTradeRecordEntities("000012");
		
		for(TradeRecordEntity tre : records){
			System.out.println(tre.getDate() + "," + tre.getPrice());
		}

	}
	
	@Test
	public void test2() {
		List<Map<String,String>> records = new ArrayList<Map<String,String>>();
		Map<String,String> record = new HashMap<String,String>();
		String code = "000012";
		String date = "2018-04-12";
		String price = "11.03";
		record.put("date", date);
		record.put("code", code);
		record.put("price", price);
		records.add(record);
		
		tradeRecordRepositoryFromQt.save(records);
		
		List<TradeRecordEntity> list = tradeRecordRepositoryFromQt.getTradeRecordEntities("000012");
		for(TradeRecordEntity entity : list) {
			System.out.println(entity);
		}

	}
	
}
