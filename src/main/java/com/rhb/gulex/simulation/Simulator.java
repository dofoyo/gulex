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
import com.rhb.gulex.service.trade.TradeRecordDTO;
import com.rhb.gulex.service.trade.TradeRecordService;
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
public class Simulator {
	
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepository;
	
	@Autowired
	TradeRecordService tradeRecordService;
	
	@Autowired
	BluechipService bluechipService;
	
	
	private  BigDecimal amount = new BigDecimal(80000); //每次买入金额,默认金额为8万
	private Integer upProbability = 50;   //买入阈值，默认50.    实际操作时，行情好时，阈值低一些，行情不好时，阈值高些
	private BigDecimal cash = new BigDecimal(1000000); //初始现金,默认为1百万
	private LocalDate beginDate = LocalDate.parse("2010-01-01");  //能找到的年报发布日期是从2010年开始的
	private boolean overdraft = false;  //是否允许透支

	
	public BigDecimal simulate(){
		System.out.println("begin simulate......");

		Trader trader = new Trader();
		trader.setAmount(amount); 
		trader.setCash(cash);
		trader.setOverdraft(overdraft);

		List<LocalDate> tradeDates = this.getTradeDate(beginDate); 
		
		Collection<TradeDetail> onHands;
		
		Iterator<TradeDetail> it;
		TradeDetail detail;
		String[] stockcodes;
		TradeRecordEntity tradeRecordEntity;
		
		int i=0;
		for(LocalDate sDate : tradeDates){
			System.out.println(i++ + "/" + tradeDates.size());
			
			
			List<BluechipDto> bluechips = bluechipService.getBluechips(sDate);
			
			//买入操作
			for(BluechipDto bluechipDto : bluechips){
				
				tradeRecordEntity = tradeRecordService.getTradeRecordEntity(bluechipDto.getCode(),sDate);
				
				if(tradeRecordEntity!=null  
					&& tradeRecordEntity.getUpProbability()>upProbability 
					&& tradeRecordEntity.isPriceOnAvarage()   
					&& ChronoUnit.DAYS.between(LocalDate.parse(bluechipDto.getIpoDate()), sDate)>365   //上市1年后才能购买
					){ 

					if(!trader.onHand(bluechipDto.getCode())){ 
						trader.buy(bluechipDto.getCode(),sDate,tradeRecordEntity.getPrice(),bluechipDto.getName());
						/*if(bluechipDto.getCode().equals("600971")){
							System.out.println(bluechipDto);
						}*/
					}
				}
			}

			//卖出操作：卖出低于120日 以上均线的票。最后一天全卖出
			onHands = trader.getOnHands();
			it = onHands.iterator();
			while(it.hasNext()){
				detail = it.next();
				tradeRecordEntity = tradeRecordService.getTradeRecordEntity(detail.getCode(),sDate);
				boolean inGoodPeriod = bluechipService.inGoodPeriod(detail.getCode(),sDate);

				
				if(tradeRecordEntity==null){
					System.out.println("tradeRecordService.getTradeRecordEntity(" + detail.getCode() + "," + sDate + ") = null ");
				}
				
				if(!inGoodPeriod && !tradeRecordEntity.isPriceOnAvarage() || i==tradeDates.size()){
					trader.sell(detail.getCode(), sDate, tradeRecordEntity.getPrice(),"");
				}else{
					trader.setPrice(detail.getCode(), tradeRecordEntity.getPrice());
				}
			}

	
			trader.dayReport(sDate);
			//System.out.println(sDate.toString() + " " + " cash = " + trader.getCash().toString() + " value = " + trader.getValue().toString());
			
		}
		

		FileUtil.writeTextFile("D:/stocks/simulation/simulate_details.csv", trader.getDetailString(), false);

		FileUtil.writeTextFile("D:/stocks/simulation/simulate_profits.csv", trader.getAccountString(), false);
	
		
		
		System.out.println("end simulate......");
		
		return trader.getCash();
		
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
	
	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Integer getUpProbability() {
		return upProbability;
	}


	public void setUpProbability(Integer upProbability) {
		this.upProbability = upProbability;
	}


	public LocalDate getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}
	
	public void setOverdraft(boolean flag){
		this.overdraft = flag;
	}
	
	
}
