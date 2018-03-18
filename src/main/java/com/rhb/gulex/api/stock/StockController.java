package com.rhb.gulex.api.stock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.service.StockService;

@RestController
public class StockController {
	@Autowired
	StockService stockService;
	
	@GetMapping("/goodStocks")
	public ResponseContent<List<StockDTO>> getGoodStocks(){
		List<StockDTO> stocks = stockService.getGoodStocks(LocalDate.now());
		
		return new ResponseContent<List<StockDTO>>(ResponseEnum.SUCCESS,stocks);
	}

}
