package com.rhb.gulex.traderecord;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.traderecord.spider.DownloadTradeRecord;
import com.rhb.gulex.traderecord.spider.DownloadTradeRecordFromQt;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class DownloadTradeRecordTest {

/*	@Autowired
	@Qualifier("DownloadTradeRecordFromQt")
	DownloadTradeRecord downloadTradeRecord;
*/	

	
	
	@Test
	public void test() {
		DownloadTradeRecordFromQt qt = new DownloadTradeRecordFromQt();
		
		
		qt.go("000858");
	}
}
