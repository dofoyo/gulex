package com.rhb.gulex.simulation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.service.StockService;

/**
 * 2006年起，年报中才有现金流报表，2008年，才有三年的现金流可以分析。
 * 因此，模拟从2009年5月1日开始。
 * 交易日期取平安银行（000001）的交易记录表上的日期。
 * 交易策略：
 * 1、每天根据年报计算一次入选名单。
 * 2、根据入选名单，计算一次上涨概率，排名第一名且上涨概率大于80%且股价在120均线上且未持仓的的，确定为该日标的股。
 * 3、如果未有该标的股都的历史交易记录，下载。
 * 4、买入标的股5万元。保存交易记录
 * 5、持仓中，股价在120均线下方的，卖出。保存交易记录
 * 6、根据股价，记录持仓的每日盈亏和累计盈亏
 * 
 * 
 * 
 * @author ran
 *
 */

@Component
public class Simulator {
	@Autowired
	StockService stockService;
	
	private Kalendar kalendar = new Kalendar();
	private Trader trader = new Trader();
	
	public void simulate(){
		List<LocalDate> tradeDates = kalendar.getTradeDate();
		StockDTO stock;
		for(LocalDate sDate : tradeDates){
			List<StockDTO>  stocks = stockService.getGoodStocks(sDate);
			stock = stocks.get(0);
			if(stock.getUpProbability()>80 && stock.isAboveAv120()){
				if(!trader.onHand(stock.getCode())){
					trader.buy(stock.getCode(),sDate,stockService.getPrice(stock.getCode(),sDate));
				}
			}
		}
		
		Set<String> onHands = trader.getOnHands();
		for(String code : onHands){
			if(!stockService.isAboveAvarage(code)){
				trader.sell(code);
			}
		}
		
		
		
		
		
	}
	
	
	
	
	
}
