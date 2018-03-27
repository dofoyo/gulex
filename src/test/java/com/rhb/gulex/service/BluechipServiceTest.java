package com.rhb.gulex.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.api.bluechip.BluechipView;
import com.rhb.gulex.service.bluechip.BluechipService;
import com.rhb.gulex.simulation.BluechipDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BluechipServiceTest {
	@Autowired
	BluechipService bluechipService;
	
	@Test
	public void test1(){
		bluechipService.generateBluechip();
	}
	
	//@Test
	public void test2(){
		List<BluechipDto> bluechips  = bluechipService.getBluechips();
		for(BluechipDto bluechip : bluechips){
			System.out.println(bluechip);
		}
		
		System.out.println("there are " + bluechips.size() + " bluechips.");
	}
	
	//@Test
	public void test3(){
		String date = "2017-04-11";
		List<BluechipView> views  = bluechipService.getBluechipViews(LocalDate.parse(date));

		for(BluechipView view : views){
			System.out.println(view);
		}
		
	}
}
