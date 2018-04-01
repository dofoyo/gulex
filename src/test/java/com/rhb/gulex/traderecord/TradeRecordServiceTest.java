package com.rhb.gulex.traderecord;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.service.TradeRecordDTO;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradeRecordServiceTest {

	@Autowired
	TradeRecordService tradeRecordService;
	
	//@Test
	public void test(){
		String stockcode="603871";
		LocalDate date = LocalDate.parse("2017-04-11");
		TradeRecordEntity tradeRecordEntity = tradeRecordService.getTradeRecordEntity(stockcode, date);
		System.out.println(tradeRecordEntity);
	}
	
	@Test
	public void test2(){
		String stockcode="603871";
		//LocalDate date = LocalDate.parse("2017-04-11");
		TradeRecordDTO tradeRecordDTO = tradeRecordService.getTradeRecordsDTO(stockcode);
		System.out.println(tradeRecordDTO);
	}
	

}
