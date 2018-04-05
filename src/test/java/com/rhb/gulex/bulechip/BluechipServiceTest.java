package com.rhb.gulex.bulechip;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.gulex.bluechip.api.BluechipCheck;
import com.rhb.gulex.bluechip.api.BluechipView;
import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.simulation.service.BluechipDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BluechipServiceTest {
	@Autowired
	BluechipService bluechipService;
	
	//@Test
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
	
	@Test
	public void test4() {
		 List<BluechipCheck> list =  bluechipService.getBluechipChecks();
		 for(BluechipCheck check : list) {
			 System.out.println(check);
		 }
	}
}
