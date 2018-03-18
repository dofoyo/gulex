package com.rhb.gulex.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.repository.financestatement.FinanceStatementsRepository;
import com.rhb.gulex.repository.stock.StockEntity;
import com.rhb.gulex.repository.stock.StockRepository;
import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;

@Service("StockServiceImp")
public class StockServiceImp implements StockService{
	@Value("${dataPath}")
	private String dataPath;
	
	@Autowired
	StockRepository stockRepository;
	
	@Autowired
	TradeRecordRepository tradeRecordRepository;
	
	@Autowired
	FinanceStatementsRepository financeStatementsRepository;
	
	Map<String,Stock> stocks;

	@Override
	public List<StockDTO> getGoodStocks(LocalDate date) {
		List<StockDTO> list = new ArrayList<StockDTO>();
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			entry.getValue().refreshFinancialStatements(date);
			
			entry.getValue().setDdate();
			
			if(entry.getValue().isGood()){
				StockDTO dto = new StockDTO();
				dto.setCode(entry.getKey());
				dto.setName(entry.getValue().getStockName());
				
				dto.setGoodPeriod(entry.getValue().getGoodPeriodString());
				dto.setGoodTimes(entry.getValue().getGoodTimes());
				dto.setLastPeriod(entry.getValue().getLastPeriod());
				dto.setUpProbability(entry.getValue().getUpProbability(date));
				dto.setAboveAv120(entry.getValue().isAboveAv120(date));
				dto.setPrice(entry.getValue().getPrice(date));
				list.add(dto);
			}
		}
		
		Collections.sort(list,new Comparator<StockDTO>(){

			@Override
			public int compare(StockDTO arg0, StockDTO arg1) {
				return arg1.getUpProbability().compareTo(arg0.getUpProbability());
			}
			
		});
		
		return list;
	}

	
	@Override
	public void init() {
		System.out.println("init begin ....");
		stocks = new HashMap<String,Stock>();
		int i=0;
		Set<StockEntity> entities = stockRepository.getStocks();
		for(StockEntity entity : entities){
			System.out.print(i++ + "/" + entities.size() + "\r");
			Stock stock = new Stock(entity.getCode(),entity.getName());
			stock.setDeleted(entity.getDeleted());
			stocks.put(entity.getCode(), stock);

			//加载年报数据
			setFinancialStatement(entity.getCode());
			
			//交易记录
			refreshMarketInfo(entity.getCode());
			
		}
		System.out.println("there are " + i + " stocks.");
		System.out.println("init end ....");
		
	}

	

	@Override
	public boolean isExist(String code) {
		return stocks.containsKey(code);
	}
	
	@Override
	public boolean isAboveAvarage(String code){
		Stock stock = stocks.get(code);
		return stock.isAboveAv120();
	}

	@Override
	public void creates(Map<String,String> codeAndNames){
		for(Map.Entry<String, String> entry : codeAndNames.entrySet()){
			Stock stock = new Stock(entry.getKey(),entry.getValue());
			stocks.put(entry.getKey(), stock);

			//年报
			setFinancialStatement(entry.getKey());
			
			//交易记录
			refreshMarketInfo(entry.getKey());

		}
		saveToRespository();
		
	}

	@Override
	public String[] getNoReportStockCode(int year) {
		List<String> list = new ArrayList<String>();
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			if(entry.getValue().getLastPeriod()<year && entry.getValue().getDeleted()==0){
				list.add(entry.getKey());
			}
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public boolean noReport(String code, Integer year) {
		Stock stock = stocks.get(code);
		return stock.getLastPeriod()<year && stock.getDeleted()==0;
	}

	@Override
	public void setFinancialStatements(String[] codes) {
		System.out.println(LocalDateTime.now() +  "   refreshFinancialStatements begin ....");
		int i=0;
		for(String code : codes){
			System.out.print(i++ + "/" + codes.length + "\r");
			this.setFinancialStatement(code);
		}
		System.out.println("there are " + i + " stocks refreshed.");
		System.out.println("refreshFinancialStatements end ....");		
	}

	@Override
	public void setFinancialStatement(String code) {
		Stock stock = stocks.get(code);
		if(stock!=null){
			stock.setBalancesheets(financeStatementsRepository.getBalanceSheets(code));
			stock.setCashflows(financeStatementsRepository.getCashFlows(code));
			stock.setProfitstatements(financeStatementsRepository.getProfitStatements(code));
			//stock.refreshFinancialStatements();
		}

	}
	@Override
	public void refreshMarketInfo(String code) {
		//System.out.println("stockServiceImp.refreshMarketInfo...");
		Stock stock = stocks.get(code);
		List<TradeRecordEntity> records = tradeRecordRepository.getTradeRecordEntities(code);
		for(TradeRecordEntity tre : records){
			stock.addTradeRecord(tre.getDate(),tre.getPrice());
		}
		stock.refreshMarketInfo();;
		
	}

	
	@Override
	public List<TradeRecord> getTradeRecords(String code) {
		Stock stock = stocks.get(code);
		return stock.getTradeRecords();
	}

	@Override
	public Integer getUpProbability(String code) {
		Stock stock = stocks.get(code);
		return stock.getUpProbability();
	}

	private void saveToRespository() {
		Set<StockEntity> stockEntities = new HashSet<StockEntity>();
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			StockEntity stockEntity = new StockEntity();
			stockEntity.setCode(entry.getKey());
			stockEntity.setName(entry.getValue().getStockName());
			stockEntity.setDeleted(entry.getValue().getDeleted());
			stockEntities.add(stockEntity);				
			//System.out.println(stock.getCode() + "," + stock.getName());
		}
		stockRepository.save(stockEntities);
	}

	@Override
	public List<StockDTO> getDeletedStocks() {
		List<StockDTO> list = new ArrayList<StockDTO>();
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			if(entry.getValue().getDeleted()==1){
				StockDTO dto = new StockDTO();
				dto.setCode(entry.getKey());
				dto.setName(entry.getValue().getStockName());
				dto.setLastPeriod(entry.getValue().getLastPeriod());
				list.add(dto);
			}
		}
		return list;
	}


	@Override
	public BigDecimal getPrice(String code, LocalDate date) {
		Stock stock = stocks.get(code);
		
		return null;
	}

	
}
