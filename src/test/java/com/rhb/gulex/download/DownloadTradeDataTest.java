package com.rhb.gulex.download;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.repository.traderecord.DownloadTradeRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DownloadTradeDataTest {
	@Autowired
	DownloadTradeRecord downloadTradeData;
	
	@Test
	public void test(){
		System.out.println("begin downloadTradeData............");
		String code = "000001";
		downloadTradeData.go(code);
		System.out.println("end downloadTradeData............");
	}
}
