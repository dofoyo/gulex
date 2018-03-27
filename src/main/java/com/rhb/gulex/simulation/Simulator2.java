package com.rhb.gulex.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Bluechip;
import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;
import com.rhb.gulex.service.bluechip.BluechipService;
import com.rhb.gulex.service.stock.StockService;
import com.rhb.gulex.util.FileUtil;

/**
 * 2006年起，年报中才有现金流报表，2008年，才有三年的现金流可以分析。
 * 能找到的年报发布日期是从2010年开始的
 * 因此，模拟从2010年1月1日开始。
 * 交易日期取上证指数（sh000001）的交易记录表上的日期。
 * 
 * 交易策略：
 * 1、每天根据年报计算一次入选名单。
 * 2、根据入选名单，计算一次上涨概率，排名第一名且上涨概率大于85%且股价在120均线上且未持仓的的，确定为该日标的股。
 * 3、如果标的股未有历史交易记录，下载。
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
public class Simulator2 {
	
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepository;
	
	@Autowired
	BluechipService bluechipService;
	
	public void simulate(){
		System.out.println("begin simulate......");

		Trader trader = new Trader();
		trader.setAmount(new BigDecimal(50000)); //每笔买入金额为5万
		trader.setCash(new BigDecimal(1000000)); //初始现金1百万

		List<LocalDate> tradeDates = this.getTradeDate(LocalDate.parse("2010-01-01")); //能找到的年报发布日期是从2010年开始的
		
		
		StockDTO dto;
		Collection<TradeDetail> onHands;
		
		Iterator<TradeDetail> it;
		TradeDetail detail;

		
		int i=0;
		for(LocalDate sDate : tradeDates){
			//System.out.println(i++ + "/" + tradeDates.size() + "\r");
			System.out.println(i++ + "/" + tradeDates.size());
			
			List<StockDTO>  dtos = stockService.getGoodStocks(sDate);
			//System.out.println("共有" + dtos.size() + "只股票于 " + sDate.toString() +" 入选");
			
			
			//买入操作
			for(StockDTO d : dtos){
				//if(d!=null && d.getUpProbability()>85 && d.isPriceAboveAv120() && d.getIpoDate().isBefore(sDate)){
				//if(d!=null && d.getUpProbability()>85 && d.isPriceAboveAv120() && ChronoUnit.DAYS.between(d.getIpoDate(), sDate)>365){ //上市1年后才能购买
				if(d!=null  
					&& d.getUpProbability()>85  
					&& d.isPriceAboveAv120()   
					&& ChronoUnit.DAYS.between(d.getIpoDate(), sDate)>365   //上市1年后才能购买
					//&& d.getIncrease().compareTo(new BigDecimal(1.5))==-1 //近300天涨幅不超过80%  避免最高
					){ 

					if(!trader.onHand(d.getCode())){ 
						trader.buy(d.getCode(),sDate,d.getPrice(),d.getName());
					}
				}
			}

			//卖出操作：卖出低于120日 以上均线的票。最后一天全卖出
			onHands = trader.getOnHands();
			it = onHands.iterator();
			while(it.hasNext()){
				detail = it.next();
				dto = stockService.getStock(detail.getCode(), sDate);
/*				if(dto.getCode().equals("600586")){
					System.out.println("金晶科技卖出判断：getGoodPeriod是否为空");
					System.out.println(dto);
				}*/
				//if(!dto.isPriceAboveAv120() || i==tradeDates.size()){
				if((dto.getGoodPeriod().isEmpty() && !dto.isPriceAboveAv120()) || i==tradeDates.size()){
					trader.sell(detail.getCode(), sDate, dto.getPrice(),dto.getName());
				}else{
					trader.setPrice(detail.getCode(), dto.getPrice());
				}
			}

	
			trader.dayReport(sDate);
			//System.out.println(sDate.toString() + " " + " cash = " + trader.getCash().toString() + " value = " + trader.getValue().toString());
			
		}
		

		FileUtil.writeTextFile("D:/stocks/simulation/simulate_details.csv", trader.getDetailString(), false);

		FileUtil.writeTextFile("D:/stocks/simulation/simulate_profits.csv", trader.getAccountString(), false);
	
		
		System.out.println("end simulate......");
		
	}
	

	private List<LocalDate> getTradeDate(LocalDate beginDate){
		String code = "sh000001"; //以上证指数的交易日期作为历史交易日历
		List<LocalDate> codes = new ArrayList<LocalDate>();
		List<TradeRecordEntity> records = tradeRecordRepository.getTradeRecordEntities(code);
		for(TradeRecordEntity record : records){
			if(record.getDate().isAfter(beginDate)){
				codes.add(record.getDate());
			}
		}
		return codes;
	}
	
	private List<Bluechip> getBluechips(){
		List<Bluechip> bluechips = new ArrayList<Bluechip>();
		bluechipService.getBluechips();
		
		
		return bluechips;
	}
	
	
	
}
