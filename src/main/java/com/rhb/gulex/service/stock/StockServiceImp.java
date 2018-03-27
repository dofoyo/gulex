package com.rhb.gulex.service.stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.domain.TradeRecord;
import com.rhb.gulex.repository.financestatement.FinanceStatementsRepository;
import com.rhb.gulex.repository.reportdate.ReportDateRepository;
import com.rhb.gulex.repository.stock.StockEntity;
import com.rhb.gulex.repository.stock.StockRepository;
import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.repository.traderecord.TradeRecordRepository;

@Service("StockServiceImp")
public class StockServiceImp implements StockService{

	@Autowired
	StockRepository stockRepository;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepository;
	
	@Autowired
	ReportDateRepository reportDateRepository;
	
	@Autowired
	FinanceStatementsRepository financeStatementsRepository;
	
	Map<String,Stock> stocks;
	
	@Override
	public StockDTO getStock(String code, LocalDate date){
		Stock stock = stocks.get(code);
		
		String goodPeriod = stock.getGoodPeriod(date.getYear()-1); //只能根据上一年的年报判断好坏。如2018年5月1日的好股票，只能根据2017年报判断

		//加载交易记录
		if(!stock.isInitMrketInfo()){
			setMarketInfo(code);
		}
		
		TradeRecord tradeRecord = stock.getTradeRecord(date);

		StockDTO dto = new StockDTO();
		dto.setCode(stock.getStockId());
		dto.setName(stock.getStockName());
		
		dto.setGoodPeriod(goodPeriod);
		dto.setGoodTimes(StringUtils.countOccurrencesOf(goodPeriod, ",") + 1);
		dto.setLastPeriod(stock.getLastPeriod());
		
		dto.setDate(date);
		if(tradeRecord!=null){
			dto.setUpProbability(tradeRecord.getUpProbability());
			dto.setAv120(tradeRecord.getAv120());
			dto.setPrice(tradeRecord.getPrice());
		}		
		
		return dto;
	}
	
	
	
	@Override
	public List<StockDTO> getGoodStocks(LocalDate date) {
		List<StockDTO> list = new ArrayList<StockDTO>();
		LocalDate reportDate=null;
		Integer year;
		for(Map.Entry<String, Stock> entry : stocks.entrySet()){
			
			//if(entry.getKey().equals("601985")){
				
				year = date.getYear()-1; //根据上一年的年报判断好坏。如2018年5月1日的好股票，只能根据2017年报判断
				String r = reportDateRepository.getReportDate(entry.getKey(), year);
				if(r != null){
					reportDate = LocalDate.parse(r);
				}
				
				//System.out.println(entry.getKey() + "," + Integer.toString(year) + "," + reportDate);
				if(reportDate!=null){ 
					if(date.isBefore(reportDate)){
						year = year - 1;   //此时如果上年度年报还没有出来，就按上上年度的年报为依据。此种情形发生在每年的5月1日前。
					}
					
					String goodPeriod = entry.getValue().getGoodPeriod(year); 
					
					//System.out.println(entry.getValue().getStockName() + "," + date.toString());
					
					if(!goodPeriod.isEmpty()){  
						//加载交易记录
						if(!entry.getValue().isInitMrketInfo()){
							setMarketInfo(entry.getKey());
						}
						
		/*				if(entry.getValue().getIpoDate()==null || entry.getValue().getIpoDate().isAfter(date)){
							System.out.println("还未上市就被选入，有问题！");
							System.out.println( "ipo日期：" + entry.getValue().getIpoDate());
							System.out.println( "选中日期：" + date.toString());
							System.out.println("");
						}*/
						
						TradeRecord tradeRecord = entry.getValue().getTradeRecord(date);
						if(tradeRecord!=null){
							StockDTO dto = new StockDTO();
							dto.setCode(entry.getKey());
							dto.setName(entry.getValue().getStockName());
							dto.setIpoDate(entry.getValue().getIpoDate());
							
							dto.setGoodPeriod(goodPeriod);
							dto.setGoodTimes(StringUtils.countOccurrencesOf(goodPeriod, ","));
							dto.setLastPeriod(entry.getValue().getLastPeriod());
							
							dto.setDate(date);
							dto.setIncrease(tradeRecord.getIncrease());
							dto.setUpProbability(tradeRecord.getUpProbability());
							dto.setAv120(tradeRecord.getAv120());
							dto.setPrice(tradeRecord.getPrice());

							list.add(dto);
						}

					}

				//}				
			}

			
		}
		
		Collections.sort(list,new Comparator<StockDTO>(){

			@Override
			public int compare(StockDTO arg0, StockDTO arg1) {
				if(arg0.getUpProbability()==null || arg1.getUpProbability()==null){
					return 0;
				}else{
					return arg1.getUpProbability().compareTo(arg0.getUpProbability());
				}
			}
			
		});
		
		return list;
	}

	
	@Override
	public void init() {
		System.out.println("init begin ....");
		String out = "000527,600840,002710,600631,000522,601206,600005";

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
				setFinancialStatement(entity.getCode());
				
				//交易记录，初始化加载太慢，还造成内存溢出
				//setMarketInfo(entity.getCode());				
			}
		}
		System.out.println("there are " + i + " stocks.");
		System.out.println("init end ....");
		
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

			//年报
			setFinancialStatement(entry.getKey());
			
			//交易记录， 初始化加载太慢，还造成内存溢出
			//setMarketInfo(entry.getKey());

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
	public void setMarketInfo(String code) {
		
		//System.out.println("stockServiceImp.refreshMarketInfo...");
		Stock stock = stocks.get(code);
		stock.setInitMrketInfo(true);
		List<TradeRecordEntity> records = tradeRecordRepository.getTradeRecordEntities(code);
		
		if(records.size()>0){
			stock.setIpoDate(records.get(0).getDate());
		}
		
		for(TradeRecordEntity tre : records){
			stock.addTradeRecord(tre.getDate(),tre.getPrice());
		}
		//stock.refreshMarketInfo();;
		
	}

	
	@Override
	public List<TradeRecord> getTradeRecords(String code) {
		Stock stock = stocks.get(code);
		return stock.getTradeRecords();
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
	public Set<String> getStockCodes() {
		return stocks.keySet();
	}



	@Override
	public StockDTO getStock(String code) {
		Stock stock = stocks.get(code);
		
		StockDTO dto = new StockDTO();
		dto.setCode(stock.getStockId());
		dto.setName(stock.getStockName());
		
		return dto;
	}



	
}
