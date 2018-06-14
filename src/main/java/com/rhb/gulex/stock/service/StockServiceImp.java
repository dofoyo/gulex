package com.rhb.gulex.stock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.financialstatement.repository.FinanceStatementsRepository;
import com.rhb.gulex.reportdate.repository.ReportDateRepository;
import com.rhb.gulex.stock.api.StockDTO;
import com.rhb.gulex.stock.repository.StockEntity;
import com.rhb.gulex.stock.repository.StockRepository;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;

@Service("StockServiceImp")
public class StockServiceImp implements StockService{

	@Autowired
	@Qualifier("StockRepositoryImp")
	StockRepository stockRepository;
	
	Map<String,Stock> stocks;
	
	@Override
	public void init() {
		System.out.println("StockServiceImp init begin ....");
		String out = "000527,600840,002710,600631,000522,601206,600005";

		
		/*
		 * 002710,慈铭体检
600631,百联股份
000522,白云山A
000527,美的电器
600840,新湖创业
600005,武钢股份
601206,海尔施
		 */
		
		stocks = new HashMap<String,Stock>();
		int i=0;
		Set<StockEntity> entities = stockRepository.getStocks();
		for(StockEntity entity : entities){
			if(out.indexOf(entity.getCode()) == -1){
				System.out.print(i++ + "/" + entities.size() + "\r");
				Stock stock = new Stock(entity.getCode(),entity.getName());
				stock.setDeleted(entity.getDeleted());
				stocks.put(entity.getCode(), stock);

				//加载年报数据
				//setFinancialStatement(entity.getCode());
				
				//交易记录，初始化加载太慢，还造成内存溢出
				//setMarketInfo(entity.getCode());				
			}
		}
		System.out.println("there are " + i + " stocks.");
		System.out.println("................StockServiceImp init end.");
		
	}



	@Override
	public boolean isExist(String code) {
		return stocks.containsKey(code);
	}
	

	@Override
	public void creates(Map<String,String> codeAndNames){
		for(Map.Entry<String, String> entry : codeAndNames.entrySet()){
			Stock stock = new Stock(entry.getKey(),entry.getValue());
			stocks.put(entry.getKey(), stock);

		}
		saveToRespository();
		
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
				list.add(dto);
			}
		}
		return list;
	}


	@Override
	public Set<String> getStockCodes() {
		return stocks.keySet();
	}



	@Override
	public StockDTO getStock(String code) {
		if(stocks == null) {
			this.init();
		}
		
		Stock stock = stocks.get(code);
		
		StockDTO dto = new StockDTO();
		dto.setCode(code);
		dto.setName(stock==null ? "" : stock.getStockName());
		
		return dto;
	}

	@Override
	public List<StockDTO> getStocks() {
		List<StockDTO> list = new ArrayList<StockDTO>();
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			if(entry.getValue().getDeleted()==1){
				StockDTO dto = new StockDTO();
				dto.setCode(entry.getKey());
				dto.setName(entry.getValue().getStockName());
				list.add(dto);
			}
		}
		return list;
	}

	
}
