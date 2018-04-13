package com.rhb.gulex.pb;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.pb.service.PbService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestPbService {
	@Autowired
	@Qualifier("PbServiceImp")
	PbService pbService;
	
	
	//@Test
	public void test() {
		List<Map<String,String>> list = pbService.getHsagPbs();
		System.out.println("date" + "," + "hsag");
		
		for(Map<String,String> map : list) {
			System.out.println(map.get("date") + "," + map.get("hsag"));
		}
	}
	
	@Test
	public void test2() {
		LocalDate date = LocalDate.parse("2014-05-05");
		System.out.println(pbService.getHsagRate(date));
	}
}
