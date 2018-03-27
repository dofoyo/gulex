package com.rhb.gulex.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.service.trade.TradeRecordDTO;
import com.rhb.gulex.service.trade.TradeRecordService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TradeRecordServiceTest {

	@Autowired
	TradeRecordService tradeRecordService;
	
	//@Test
	public void test(){
		String stockcode="000539";
		LocalDate date = LocalDate.parse("2017-04-11");
		TradeRecordEntity tradeRecordEntity = tradeRecordService.getTradeRecordEntity(stockcode, date);
		System.out.println(tradeRecordEntity);
	}
	
	//@Test
	public void test2(){
		String stockcode="000539";
		LocalDate date = LocalDate.parse("2017-04-11");
		TradeRecordDTO tradeRecordDTO = tradeRecordService.getTradeRecordsDTO(stockcode);
		System.out.println(tradeRecordDTO);
	}
	
	@Test
	public void test3(){
		String stockcode="600754";
		LocalDate date = LocalDate.parse("2018-03-26");
		BigDecimal midPrice = tradeRecordService.getMidPrice(stockcode, date);
		System.out.println(midPrice);
	}

}
