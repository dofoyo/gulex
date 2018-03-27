package com.rhb.gulex.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradeRecordRepositoryTest {
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepository;
	
	@Test
	public void test(){
		List<TradeRecordEntity> records = tradeRecordRepository.getTradeRecordEntities("000012");
		
		for(TradeRecordEntity tre : records){
			System.out.println(tre.getDate() + "," + tre.getPrice());
		}

	}
	
}
