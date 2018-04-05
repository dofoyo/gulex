package com.rhb.gulex.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.ScheduledTask;
import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;

@RestController
public class GulexController {
	@Autowired
	ScheduledTask scheduledTask;
	
	@GetMapping("/refresh")
	public ResponseContent<String> refresh(){
		String str = "just do it";
		
		scheduledTask.doAll();
		
		return new ResponseContent<String>(ResponseEnum.SUCCESS, str);
	}

}
