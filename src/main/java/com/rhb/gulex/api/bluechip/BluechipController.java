package com.rhb.gulex.api.bluechip;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.service.bluechip.BluechipService;

@RestController
public class BluechipController {
	@Autowired
	BluechipService bluechipService;
	
	@GetMapping("/bluechips")
	public ResponseContent<List<BluechipView>> getBluechipViews(
			@RequestParam(value="date", defaultValue="") String date
			){
		LocalDate theDate = null;
		if(date.isEmpty()){
			theDate = LocalDate.now();
		}else{
			//System.out.println(date);
			theDate = LocalDate.parse(date.substring(0,10));
		}
		List<BluechipView> bluechips = bluechipService.getBluechipViews(theDate);
		//System.out.println(bluechips.size());
		return new ResponseContent<List<BluechipView>>(ResponseEnum.SUCCESS, bluechips);
	}

}
