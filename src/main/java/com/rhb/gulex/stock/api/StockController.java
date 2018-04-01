package com.rhb.gulex.stock.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.gulex.api.ResponseContent;
import com.rhb.gulex.api.ResponseEnum;
import com.rhb.gulex.stock.service.StockService;

@RestController
public class StockController {
	@Autowired
	@Qualifier("StockServiceImp")
	StockService stockService;
	
	@GetMapping("/stocks")
	public ResponseContent<List<StockDTO>> getStocks(){
		List<StockDTO> stocks = stockService.getStocks();
		
		return new ResponseContent<List<StockDTO>>(ResponseEnum.SUCCESS,stocks);
	}

}
