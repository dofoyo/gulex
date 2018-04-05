package com.rhb.gulex.bluechip.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.bluechip.api.BluechipCheck;
import com.rhb.gulex.bluechip.api.BluechipView;
import com.rhb.gulex.bluechip.repository.BluechipEntity;
import com.rhb.gulex.bluechip.repository.BluechipRepository;
import com.rhb.gulex.financialstatement.service.FinancialStatement;
import com.rhb.gulex.financialstatement.service.FinancialStatementService;
import com.rhb.gulex.financialstatement.service.OkfinanceStatementDto;
import com.rhb.gulex.reportdate.repository.ReportDateRepository;
import com.rhb.gulex.simulation.service.BluechipDto;
import com.rhb.gulex.stock.api.StockDTO;
import com.rhb.gulex.stock.service.StockService;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;
import com.rhb.gulex.traderecord.service.TradeRecordDTO;
import com.rhb.gulex.traderecord.service.TradeRecordService;

@Service("BluechipServiceImp")
public class BluechipServiceImp implements BluechipService {

	@Autowired
	@Qualifier("FinancialStatementServiceImp")
	FinancialStatementService financialStatementService;
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;
	
	@Autowired
	@Qualifier("BluechipRepositoryImp")
	BluechipRepository bluechipRepository;
	
	@Autowired
	@Qualifier("ReportDateRepositoryImp")
	ReportDateRepository reportDateRepository;
	
	@Autowired
	@Qualifier("StockServiceImp")
	StockService stockService;
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFromDzh")
	TradeRecordRepository tradeRecordRepositoryFromDzh;

	@Autowired
	@Qualifier("TradeRecordRepositoryImpFrom163")
	TradeRecordRepository tradeRecordRepositoryFrom163;
	
	private List<Bluechip> bluechips = null;
	
	@Override
	public void generateBluechip() {
		System.out.println("generate Bluechips begin ....");
		
		Map<String,BluechipEntity>bluechips = new HashMap<String,BluechipEntity>();
		BluechipEntity bluechip;
		LocalDate ipoDate;
		Integer ipoYear;
		Integer okYear;
		StockDTO stockdto;
		
		int i=0;
		List<OkfinanceStatementDto> dtos = financialStatementService.getOks();
		for(OkfinanceStatementDto dto : dtos){
			System.out.print(i++ + "/" + dtos.size() + "dtos.size" + "\r");
			
			if(bluechips.containsKey(dto.getStockcode())){
				bluechip = bluechips.get(dto.getStockcode());
			}else{
				stockdto = stockService.getStock(dto.getStockcode());
				
				bluechip = new BluechipEntity();
				bluechip.setCode(dto.getStockcode());
				bluechip.setName(stockdto.getName());

			}
			bluechip.addOkYear(dto.getYear());
			bluechip.setReportDates(reportDateRepository.getReportDates(dto.getStockcode()));
			
			bluechips.put(dto.getStockcode(), bluechip);

		}
		
		bluechipRepository.save(bluechips.values());
		//System.out.println("there are " + dtos.size() + " OkfinanceStatementDtos.");
		System.out.println("there are " + bluechips.size() + " bluechips.");
		System.out.println("..................generate Bluechips end");
		
	}
	
	@Override
	public void init(){
		System.out.println("init Bluechips begin ....");
		this.bluechips = new ArrayList<Bluechip>();
		Set<BluechipEntity> entities = bluechipRepository.getBluechips();
		LocalDate ipoDate;
		Bluechip bluechip;
		Set<Integer> okYears;
		int i=0;
		for(BluechipEntity entity : entities){
			System.out.print(i++ + "/" + entities.size() + "\r");
			
			ipoDate = tradeRecordService.getIpoDate(entity.getCode()); 
			
			//如果ipoDate==null，说明该股还未上市，还无法初始化bluechip
			if(ipoDate!=null) { 
				okYears = entity.getOkYears(ipoDate.getYear());
				//如果okYears都在ipoDate之前，入选无意义
				if(okYears.size()>0) {
					bluechip = new Bluechip();
					bluechip.setCode(entity.getCode());
					bluechip.setName(entity.getName());
					bluechip.setIpoDate(ipoDate);
					bluechip.setOkYears(okYears);
					
					if(entity.getReportDates()!=null) {
						for(Map.Entry<Integer, String> entry : entity.getReportDates().entrySet()){
							bluechip.addReportDate(entry.getKey(), LocalDate.parse(entry.getValue()));
						}
					}
					this.bluechips.add(bluechip);					
				}

			}
		}
		
		System.out.println("there are " + entities.size() + " bluechips.");
		System.out.println("...........init Bluechips end ");
	}
	
	@Override
	public List<BluechipDto> getBluechips() {
		if(this.bluechips == null){
			init();
		}
		
		List<BluechipDto> dtos = new ArrayList<BluechipDto>();
		for(Bluechip bluechip : this.bluechips){
			BluechipDto dto = this.getDto(bluechip);
			dtos.add(dto);
		}
		
		return dtos;
		
	}

	/*
	 * 选股策略：
	 * 1、当年入选
	 * 2、近三年二次入选
	 * 满足以上一条即可
	 * 
	 * 例如：传入的日期是2018年3月24日
	 * 则如下条件选中：
	 * 1、2017年年报OK（已发布2017年年报）
	 * 2、2016年和2015年OK
	 * 满足以上一条即可
	 * 
	 * 或
	 * 
	 * 1、2016年年报OK（还未发布2017年年报）
	 * 2、2015年和2014年OK
	 * 满足以上一条即可
	 * 
	 * 
	 */
	private List<BluechipDto> getBluechipDtos(LocalDate date) {
		if(this.bluechips == null){
			init();
		}
		
		Integer year = date.getYear() - 1; //当前只能依据往年的年报进行判断。

		boolean isgood = false;
		
		List<BluechipDto> dtos = new ArrayList<BluechipDto>();
		for(Bluechip bluechip : this.bluechips){
			isgood = false;
			
			
			if(bluechip.getIpoDate()!=null && date.isAfter(bluechip.getIpoDate())){
				//判断是否发布年报
				if(bluechip.hasReported(date)){
					//if(bluechip.isOk(year) || bluechip.isOk(year-1) || bluechip.isOk(year-2)){
					if(bluechip.isOk(year) || (bluechip.isOk(year-1) && bluechip.isOk(year-2))){
					//if(bluechip.isOk(year)){
						//选中
						isgood = true;
/*						str = "已发布"+year+"年报";
						if(bluechip.isOk(year)){
							str = str + "以" + Integer.toString(year) + "年报入选";
						}else{
							str = str + "以" + Integer.toString(year-1) + "和" + Integer.toString(year-2) + "年报入选";
						}
*/					}
				}else{
					//if(bluechip.isOk(year-1) || bluechip.isOk(year-2) || bluechip.isOk(year-3)){
					if(bluechip.isOk(year-1) || (bluechip.isOk(year-2) && bluechip.isOk(year-3))){
					//if(bluechip.isOk(year-1)){
						//选中
						isgood = true;
/*						str = "还未发布"+year+"年报";
						if(bluechip.isOk(year-1)){
							str = str + "以" + Integer.toString(year-1) + "年报入选";
						}else{
							str = str + "以" + Integer.toString(year-2) + "和" + Integer.toString(year-3) + "年报入选";
						}
*/					}					
				}
				
				if(isgood){
					//System.out.println(date.toString() + str + " : " + bluechip);
					BluechipDto dto = this.getDto(bluechip);
					/*if(dto.getCode().equals("600971")){
						System.out.println("It is good on" + date);
						System.out.println(dto);
					}*/
					dtos.add(dto);
				};
				
			}
		}
		
		return dtos;
	}

	@Override
	public List<BluechipDto> getBluechips(LocalDate date) {
		List<BluechipDto> bluechips = this.getBluechipDtos(date);
		/*for(BluechipDto bluechipDto : bluechips){
			TradeRecordDTO tradeRecordDto = tradeRecordService.getTradeRecordsDTO(bluechipDto.getCode());
			bluechipDto.setTradeRecordEntity(tradeRecordDto.getTradeRecordEntity(date));
		}*/
		
		return bluechips;
	}
	
	
	
	private BluechipDto getDto(Bluechip bluechip){
		BluechipDto dto = new BluechipDto();
		dto.setCode(bluechip.getCode());
		dto.setName(bluechip.getName());
		dto.setIpoDate(bluechip.getIpoDate()==null ? null : bluechip.getIpoDate().toString());
		dto.setOkYears(bluechip.getOkYears());
		//dto.setReportDates(bluechip.getReportDates());
		for(Map.Entry<Integer, LocalDate> entry : bluechip.getReportDates().entrySet()){
			dto.addReportDate(entry.getKey(), entry.getValue().toString());
		}
		return dto;

	}

	@Override
	public boolean inGoodPeriod(String stockcode, LocalDate date) {
		boolean flag = false;
		
		List<BluechipDto> dtos = getBluechipDtos(date);
		for(BluechipDto dto : dtos){
			if(dto.getCode().equals(stockcode)){

				flag = true;
				break;
			}
		}
		
		return flag;
	}

	@Override
	public List<BluechipView> getBluechipViews(LocalDate date) {
		//System.out.println("getBluechipViews(" + date.toString() + ")");
		List<BluechipView> views = new ArrayList<BluechipView>();
		TradeRecordDTO tradeRecordDto;

		//System.out.println("getBluechipDtos start ...");  
		//long startTime = System.currentTimeMillis(); // 获取开始时间  
		List<BluechipDto> bluechips = this.getBluechipDtos(date);
		//long endTime = System.currentTimeMillis(); // 获取结束时间  
	    //System.out.println("getBluechipDtos over. 程序运行时间： " + (endTime - startTime) + "ms");  

		for(BluechipDto bluechipDto : bluechips){
			BluechipView view = new BluechipView();
			view.setCode(bluechipDto.getCode());
			view.setName(bluechipDto.getName());
			view.setOkYears(bluechipDto.getOkYearString());
			view.setDate(date.toString());
			
			tradeRecordDto = tradeRecordService.getTradeRecordsDTO(bluechipDto.getCode());
			if(tradeRecordDto != null) {
				view.setUpProbability(tradeRecordDto.getSimilarTradeRecordEntity(date).getUpProbability());
				view.setAboveAv120Days(tradeRecordDto.getSimilarTradeRecordEntity(date).getAboveAv120Days());
				view.setBiasOfAv120(tradeRecordDto.getSimilarTradeRecordEntity(date).getBiasOfAv120());
				view.setBiasOfMidPrice(tradeRecordDto.getSimilarTradeRecordEntity(date).getBiasOfMidPrice());
			}
			
			views.add(view);
		}
		
		Collections.sort(views, new Comparator<BluechipView>(){

			@Override
			public int compare(BluechipView arg0, BluechipView arg1) {
				return arg1.getUpProbability().compareTo(arg0.getUpProbability());
			}
			
		});
		return views;
	}

	@Override
	public BluechipDto getBluechips(String stockcode) {
		BluechipDto bd = null;
		List<BluechipDto> dtos = getBluechips();
		for(BluechipDto dto : dtos){
			if(dto.getCode().equals(stockcode)){
				bd = dto;
			}
		}
		return bd;
	}
	
	/*
	 * 需要解决的问题：
	 * 1、ipoDate is null
	 * 2、reportDate is null
	 * 3、除权后没有及时更新traderecord
	 * 
	 * 解决措施
	 * 1、ipoDate is null
	 * 主要是新入选的股票，没有完整的traderecord,
	 * 解决办法是，立即从163直接下载。163的traderecord可以解决ipoDate，ipodate不用保存，每次初始化traderecord时，也就完成了ipodate的初始化
	 * 
	 */
	
	
	@Override
	public List<BluechipCheck> getBluechipChecks() {
		if(this.bluechips == null){
			init();
		}
		
		List<BluechipCheck> checks = new ArrayList<BluechipCheck>();
		
		
		BluechipCheck check;
		LocalDate ipoDate;
		
		List<TradeRecordEntity> dzh;
		List<TradeRecordEntity> e163;
		TradeRecordEntity tradeRecordEntity;
		
		Map<Integer, String> reportdates;
		List<Integer> years;
		StringBuffer reportDateError;
		
		for(Bluechip chip : bluechips) {
			check = new BluechipCheck();
			check.setCode(chip.getCode());
			check.setName(chip.getName());
			check.setOkYears(chip.getOkYearString());
			check.setIpoDate(chip.getIpoDate()==null ? "NULL" : chip.getIpoDate().toString());
			check.setReportDate(chip.getReportDateString());
			
			dzh = tradeRecordRepositoryFromDzh.getTradeRecordEntities(check.getCode());
			if(dzh!=null) {
				tradeRecordEntity = dzh.get(dzh.size()-1);
				check.setDhzDate(tradeRecordEntity.getDate().toString());
			}
			
			e163 = tradeRecordRepositoryFrom163.getTradeRecordEntities(check.getCode()); 
			tradeRecordEntity = e163.get(e163.size()-1);
			check.setWyDate(tradeRecordEntity.getDate().toString());
			
			reportDateError = new StringBuffer();
			reportdates = reportDateRepository.getReportDates(check.getCode());
			years = financialStatementService.getPeriods(check.getCode());
			for(Integer year : years) {
				if(year>2008 && !reportdates.containsKey(year)) {
					reportDateError.append(year);
					reportDateError.append(",");
				}
			}
			check.setReportDateError(reportDateError.length()>0 ? "no reportDate:" + reportDateError.toString() : "");
			
			System.out.println(check);
			
			checks.add(check);

		}
		
		return checks;
	}
	

/*	@Override
	public List<BluechipCheck> getBluechipChecks() {
		List<BluechipCheck> checks = new ArrayList<BluechipCheck>();
		
		Set<BluechipEntity> entities = bluechipRepository.getBluechips();
		
		BluechipCheck check;
		LocalDate ipoDate;
		
		List<TradeRecordEntity> dzh;
		List<TradeRecordEntity> e163;
		TradeRecordEntity tradeRecordEntity;
		
		Map<Integer, String> reportdates;
		List<Integer> years;
		StringBuffer reportDateError;
		
		for(BluechipEntity entity : entities) {
			check = new BluechipCheck();
			check.setCode(entity.getCode());
			check.setName(entity.getName());
			check.setOkYears(entity.getOkYearString());
			
			ipoDate = tradeRecordService.getIpoDate(check.getCode());
			check.setIpoDate(ipoDate==null ? "" : ipoDate.toString());
			
			dzh = tradeRecordRepositoryFromDzh.getTradeRecordEntities(check.getCode());
			if(dzh!=null) {
				tradeRecordEntity = dzh.get(dzh.size()-1);
				check.setDhzDate(tradeRecordEntity.getDate().toString());
			}
			
			e163 = tradeRecordRepositoryFrom163.getTradeRecordEntities(check.getCode()); 
			tradeRecordEntity = e163.get(e163.size()-1);
			check.setWyDate(tradeRecordEntity.getDate().toString());
			
			reportDateError = new StringBuffer("no reportDate:");
			reportdates = reportDateRepository.getReportDates(check.getCode());
			years = financialStatementService.getPeriods(check.getCode());
			for(Integer year : years) {
				if(year>2008 && !reportdates.containsKey(year)) {
					reportDateError.append(year);
					reportDateError.append(",");
				}
			}
			check.setReportDateError(reportDateError.toString());
			
			System.out.println(check);
			
			checks.add(check);

		}
		
		return checks;
	}*/
}
