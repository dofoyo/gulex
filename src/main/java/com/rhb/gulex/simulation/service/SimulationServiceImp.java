package com.rhb.gulex.simulation.service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.pb.service.PbService;
import com.rhb.gulex.simulation.api.OnHandView;
import com.rhb.gulex.simulation.api.SimulationView;
import com.rhb.gulex.simulation.api.SimulationViewPlus;
import com.rhb.gulex.simulation.api.ValueView;
import com.rhb.gulex.stock.service.StockService;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;
import com.rhb.gulex.traderecord.service.TradeRecordService;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.LineChart;
import com.rhb.gulex.util.LineChartDTO;

/**
 * 
 * @author ran
 * 买卖操作模拟器
 * 每天收盘后执行一次
 *
 *
 */

@Service("SimulationServiceImp")
public class SimulationServiceImp implements SimulationService {
	@Value("${dataPath}")
	private String dataPath;
	
	private String imagePath = "simulation/images/";
	
	@Autowired
	@Qualifier("StockServiceImp")
	StockService stockService;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFrom163")
	TradeRecordRepository tradeRecordRepositoryFrom163;
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	@Autowired
	@Qualifier("BluechipServiceImp")
	BluechipService bluechipService;
	
	
	@Autowired
	@Qualifier("PbServiceImp")
	PbService pbService;
	
	
	private SimulationSettings settings = new SimulationSettings();
	
	private Trader trader = null;
	
	private TradeDetail detail;
	private TradeRecordEntity tradeRecordEntity;
	
	private Integer buyValve = null;
	
	@Override
	public void init(){
		System.out.println("SimulationService init begin......");
		Long start = System.currentTimeMillis();

		trader = new Trader(settings.getBeginDate(),settings.getCash());
		trader.setBuyValvePeriod(settings.getBuyValvePeriod());
		
		trader.setFinancing(settings.isFinancing());
		trader.setAmountFix(settings.isAmountFix());
		trader.setFixAmount(settings.getFixAmount());
		trader.setFixRate(settings.getFixRate());
		
		
		List<LocalDate> tradeDates = tradeRecordService.getTradeDate(settings.getBeginDate());
		
		int i=0;
		for(LocalDate sDate : tradeDates){
			//System.out.println(sDate.toString());
			System.out.print(i++ + "/" + tradeDates.size() + "\r");
			this.trade(sDate);
			
		}

		Long end = System.currentTimeMillis();

		FileUtil.writeTextFile(dataPath + "simulation/simulate_details"+end.toString()+".csv", trader.getDetailString(), false);

		FileUtil.writeTextFile(dataPath + "simulation/simulate_profits"+end.toString()+".csv", trader.getAccountString(), false);

		System.out.println("there are " + tradeDates.size() + " trade dates happened.");

		System.out.println("simulate 运行时间： "+(end-start)+"ms");

		System.out.println(".......SimulationService init end......");
		
	}
	
	/**
	 * 先将onHand的价格赋予最新价格
	 * 再看看是否有触及卖出条件的，有的话，卖出，腾出资金
	 * 然后买入
	 * 买入后，再判断是否有超出持股数量限制，如是，卖出收益垫底的股票
	 * 
	 * 最后，日结
	 */
	@Override
	public void trade(LocalDate sDate) {
		List<BluechipDto> bluechips = bluechipService.getBluechips(sDate);

		this.buyValve = settings.isAutoValveByPb() ? pbService.getBuyValve(sDate) : settings.getBuyValve(trader.getWinLossRatio());
		
		//将最新值写入onHand
		List<TradeDetail> onHandDetails = trader.getOnHandsList();
		for(TradeDetail detail : onHandDetails) {
			tradeRecordEntity = tradeRecordService.getSimilarTradeRecordEntity(detail.getCode(),sDate);
			trader.setPrice(detail.getSeriesid(),tradeRecordEntity.getPrice() );

		}
		
		//卖出操作：落选且股价低于60日均线的票，或止损。
		Integer profitRate;	
		boolean doSell = false;
		boolean inGoodPeriod;
		String note = null;
		
		for(TradeDetail detail : onHandDetails) {
			tradeRecordEntity = tradeRecordService.getTradeRecordEntity(detail.getCode(),sDate);
					
			if(tradeRecordEntity!=null) {   //停牌期间不能卖出
				doSell = false;
				note = "";
				inGoodPeriod = bluechipService.inGoodPeriod(detail.getCode(),sDate);

				profitRate = trader.getOnHandProfitRate(detail.getCode());
				
				if(!inGoodPeriod && !tradeRecordEntity.isPriceOnAv(settings.getSellLine())){
					note = "落选且股价低于"+settings.getSellLine()+"均线";
					doSell = true;
					
				}else if (settings.isStopLoss() && profitRate<settings.getStopLossRate()){
					note = "止损，收益率为" + profitRate.toString() + ",低于" + settings.getStopLossRate().toString();
					doSell = true;
				}
				
				if(doSell) { 
					trader.sell(detail.getSeriesid(), sDate, tradeRecordEntity.getPrice(),note);
					generateImage("卖出",detail.getCode(),detail.getName(),sDate,tradeRecordService.getTradeRecords(detail.getCode(), sDate));
				}				
			}
			
/*			if(sDate.equals(LocalDate.parse("2018-05-14"))) {
				System.out.println(detail.getCode());
				System.out.println(tradeRecordEntity);
				System.out.println(note);
			}*/
			
		}
		
		//买入操作
		boolean doBuy = false;
		for(BluechipDto bluechipDto : bluechips){
			tradeRecordEntity = tradeRecordService.getTradeRecordEntity(bluechipDto.getCode(),sDate);
			if(tradeRecordEntity!=null) {   //停牌期间不能买入
				doBuy = false;
				note="";
				
				if(tradeRecordEntity!=null  
					&& tradeRecordEntity.getUpProbability()> buyValve
					&& tradeRecordEntity.isPriceOnAv(settings.getBuyLine())   
					&& ChronoUnit.DAYS.between(LocalDate.parse(bluechipDto.getIpoDate()), sDate)>settings.getNoBuyDays() 
					){ 

					note = tradeRecordEntity.getUpProbabilityString();
					
					if(!trader.onHand(bluechipDto.getCode())){
						doBuy = true;
					}else if(settings.isAddMore() && trader.getOnHandLowestProfitRate(bluechipDto.getCode())>settings.getAddMoreThan()){ 
						note = note + "，加仓买入。";
						doBuy = true;
					}
				}
				
				if(doBuy) { 
					trader.buy(bluechipDto.getCode(),bluechipDto.getName(),sDate,tradeRecordEntity.getPrice(),note);
					generateImage("买入", bluechipDto.getCode(),bluechipDto.getName(),sDate,tradeRecordService.getTradeRecords(bluechipDto.getCode(), sDate));
				}			
			}
		}
		
		//限数卖出
		TradeDetail tradeDetail;
		if(settings.isOnHandsLimit()) {
			note = "超出持股数量，卖出表现最差的股票";
			List<String> outers = trader.getOuters(settings.getOnHandsLimitNumber());
			
			for(String seriesid : outers) {
				tradeDetail = trader.getOnHandTradeDetail(seriesid);
				tradeRecordEntity = tradeRecordService.getTradeRecordEntity(tradeDetail.getCode(),sDate);
				if(tradeRecordEntity != null) {
					trader.sell(seriesid, sDate, note);
				}
			}		
		}


		//日结
		trader.dayReport(sDate, buyValve);		
	}
	
	
	private void generateImage(String operation,String stockCode, String stockName,LocalDate date,List<TradeRecordEntity> tradeRecordEntities) {
		File file = new File(dataPath + imagePath + stockCode +"_"+ date.toString() + ".png");
		String title = operation + stockCode + "(" + stockName + ")";
		List<LineChartDTO> datas = new ArrayList<LineChartDTO>();
		for(TradeRecordEntity tradeRecordEntity : tradeRecordEntities) {
			datas.add(new LineChartDTO(tradeRecordEntity.getPrice().doubleValue(),"price",tradeRecordEntity.getDate().toString()));
			datas.add(new LineChartDTO(tradeRecordEntity.getAv120().doubleValue(),"av120",tradeRecordEntity.getDate().toString()));
		}
		LineChart.draw(title, file, datas);
	}

	@Override
	public List<SimulationView> getTradeRecordViews() {
		if(trader == null){
			this.init();
		}
		List<SimulationView> views = new ArrayList<SimulationView>();
		
		List<TradeDetail> details = this.trader.getSimulationDetails();
		
		//details.putAll(this.trader.getOnHands());
		
		for(TradeDetail detail : details) {
			SimulationView buyView = new SimulationView();
			buyView.setDate(detail.getBuyDate().toString());
			buyView.setStockcode(detail.getCode());
			buyView.setStockname(detail.getName());
			buyView.setQuantity(detail.getQuantity());
			buyView.setBuyorsell("买入");
			buyView.setPrice(detail.getBuyCost());
			views.add(buyView);
			
			if(detail.getSellDate()!=null){
				SimulationView sellView = new SimulationView();
				sellView.setDate(detail.getSellDate().toString());
				sellView.setStockcode(detail.getCode());
				sellView.setStockname(detail.getName());
				sellView.setQuantity(-1*detail.getQuantity());
				sellView.setBuyorsell("卖出");
				sellView.setPrice(detail.getSellPrice());
				views.add(sellView);				
			}
			
			
		}
			
		return views;
	}

	@Override
	public List<OnHandView> getOnHandViews() {
		if(trader == null){
			this.init();
		}
		
		List<OnHandView> views = new ArrayList<OnHandView>();
		List<TradeDetail> details = trader.getOnHandsList();
		for(TradeDetail detail : details) {
			OnHandView view = new OnHandView();
			view.setCode(detail.getCode());
			view.setTradeDate(detail.getBuyDate().toString());
			view.setName(detail.getName());
			view.setQuantity(detail.getQuantity());
			view.setCost(detail.getBuyCost());
			view.setPrice(detail.getSellPrice());
			views.add(view);
		}
		
		return views;
	}

	@Override
	public List<ValueView> getDayValueViews() {
		if(trader == null){
			this.init();
		}
		List<ValueView> views = new ArrayList<ValueView>();
		List<Account> accounts = trader.getAccounts();
		for(Account account : accounts) {
			ValueView view = new ValueView();
			view.setDate(account.getDate().toString());
			view.setValue(account.getValue());
			view.setCash(account.getCash());
			views.add(view);
		}
		
		return views;
	}
	
	
	@Override
	public List<SimulationViewPlus> getTradeRecordViewPlus() {
		if(trader == null){
			this.init();
		}
		List<SimulationViewPlus> views = new ArrayList<SimulationViewPlus>();
		
		List<TradeDetail> details = this.trader.getSimulationDetails();
		
		SimulationViewPlus view;
		for(TradeDetail detail : details) {
			view = new SimulationViewPlus();
			view.setStockcode(detail.getCode());
			view.setStockname(detail.getName());
			view.setQuantity(detail.getQuantity());
			view.setBuyDate(detail.getBuyDate().toString());
			view.setBuyPrice(detail.getBuyCost());
			view.setSellDate(detail.getSellDate()==null ? "" : detail.getSellDate().toString());
			view.setSellPrice(detail.getSellPrice());
			
			views.add(view);
		}
		
		return views;
	}

	@Override
	public List<ValueView> getYearValueViews() {
		if(trader == null){
			this.init();
		}
/*		Set<LocalDate> period = new HashSet<LocalDate>();
		List<LocalDate> tradeDates = tradeRecordService.getTradeDate(settings.getBeginDate());
		
		LocalDate date;
		LocalDate previousDate = settings.getBeginDate();
		Integer previousYear = settings.getBeginDate().getYear();
		for(int i=0; i<tradeDates.size(); i++) {
			date = tradeDates.get(i);
			if(date.getYear() != previousYear) {
				period.add(previousDate);
			}
			previousDate = date;
			previousYear = date.getYear();
			
		}*/
		
		//Integer now = LocalDate.now().getYear();
		Integer previousYear = settings.getBeginDate().getYear();
		Account previousAccount=null;

		List<ValueView> views = new ArrayList<ValueView>();
		List<Account> accounts = trader.getAccounts();
		for(Account account : accounts) {
			if(previousAccount != null && account.getDate().getYear() != previousYear) {
				ValueView view = new ValueView();
				view.setDate(previousAccount.getDate().toString());
				view.setValue(previousAccount.getValue());
				view.setCash(previousAccount.getCash());
				views.add(view);				
			}
			previousAccount = account;
			previousYear = account.getDate().getYear();
			
		}
		
		if(previousAccount != null) {
			ValueView view = new ValueView();
			view.setDate(previousAccount.getDate().toString());
			view.setValue(previousAccount.getValue());
			view.setCash(previousAccount.getCash());		
			views.add(view);				
		}
		
		return views;
	}



	@Override
	public BigDecimal getTotal() {
		return trader.getTotal();
	}

	@Override
	public void setAutoBuyValve(boolean autoBuyValve) {
		this.settings.setAutoBuyValve(autoBuyValve);
	}
	
	public void setBuyValve(Integer buyValve) {
		this.settings.setBuyValve(buyValve);
	}
	
	public void setOnHandsLimit(boolean onHandsLimit) {
		this.settings.setOnHandsLimit(onHandsLimit);
	}

	public void setOnHandsLimitNumber(Integer onHandsLimitNumber) {
		this.settings.setOnHandsLimitNumber(onHandsLimitNumber);
	}

	public void setFinancing(boolean financing) {
		this.settings.setFinancing(financing);
	}
	
	public void setStopLoss(boolean stopLoss) {
		this.settings.setStopLoss(stopLoss);
	}

	public void setBuyLinePeriod(Integer period) {
		this.settings.setBuyValvePeriod(period);
	}

	public void setStopLossRate(Integer stopLossRate) {
		this.settings.setStopLossRate(stopLossRate);
	}


	public void setBeginDate(LocalDate beginDate) {
		this.settings.setBeginDate(beginDate);
	}
	
	public String getSettingString() {
		return this.settings.toString();
	}

	@Override
	public String getBuyValue() {
		return this.buyValve.toString();
	}

}
