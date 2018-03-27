package com.rhb.gulex.service.bluechip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhb.gulex.api.bluechip.BluechipView;
import com.rhb.gulex.api.stock.StockDTO;
import com.rhb.gulex.domain.Bluechip;
import com.rhb.gulex.repository.bluechip.BluechipEntity;
import com.rhb.gulex.repository.bluechip.BluechipRepository;
import com.rhb.gulex.repository.reportdate.ReportDateRepository;
import com.rhb.gulex.repository.traderecord.TradeRecordEntity;
import com.rhb.gulex.service.fin.FinancialStatementService;
import com.rhb.gulex.service.fin.OkfinanceStatementDto;
import com.rhb.gulex.service.stock.StockService;
import com.rhb.gulex.service.trade.TradeRecordDTO;
import com.rhb.gulex.service.trade.TradeRecordService;
import com.rhb.gulex.simulation.BluechipDto;

@Service("BluechipServiceImp")
public class BluechipServiceImp implements BluechipService {

	@Autowired
	FinancialStatementService financialStatementService;
	
	@Autowired
	TradeRecordService tradeRecordService;
	
	@Autowired
	BluechipRepository bluechipRepository;
	
	@Autowired
	ReportDateRepository reportDateRepository;
	
	@Autowired
	StockService stockService;
	
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
			System.out.print(i++ + "/" + dtos.size() + "\r");
			
			if(bluechips.containsKey(dto.getStockcode())){
				bluechip = bluechips.get(dto.getStockcode());
				
				if(bluechip.getIpoDate()==null){
					bluechip.addOkYear(dto.getYear());
				}else{
					ipoYear = LocalDate.parse(bluechip.getIpoDate()).getYear();
					okYear =  dto.getYear();
					if(okYear>=ipoYear){
						bluechip.addOkYear(dto.getYear());
					}
				}
				
			}else{
				stockdto = stockService.getStock(dto.getStockcode());
				
				BluechipEntity newBlue = new BluechipEntity();
				newBlue.setCode(dto.getStockcode());
				newBlue.setName(stockdto.getName());
				ipoDate = tradeRecordService.getIpoDate(dto.getStockcode());
				newBlue.setIpoDate(ipoDate.toString());
				
				if(ipoDate == null){
					newBlue.addOkYear(dto.getYear());
					newBlue.setReportDates(reportDateRepository.getReportDates(dto.getStockcode()));
					bluechips.put(dto.getStockcode(), newBlue);
				}else{
					ipoYear = ipoDate.getYear();
					okYear =  dto.getYear();
					if(okYear>=ipoYear){
						newBlue.addOkYear(dto.getYear());
						newBlue.setReportDates(reportDateRepository.getReportDates(dto.getStockcode()));
						bluechips.put(dto.getStockcode(), newBlue);
					}
				}
			}
		}
		
		bluechipRepository.save(bluechips.values());
		System.out.println("there are " + dtos.size() + " OkfinanceStatementDtos.");
		System.out.println("there are " + bluechips.size() + " bluechips.");
		System.out.println("..................generate Bluechips end");
		
	}
	
	public void init(){
		this.bluechips = new ArrayList<Bluechip>();
		Set<BluechipEntity> entities = bluechipRepository.getBluechips();
		for(BluechipEntity entity : entities){
			Bluechip bluechip = new Bluechip();
			bluechip.setCode(entity.getCode());
			bluechip.setName(entity.getName());
			bluechip.setIpoDate(LocalDate.parse(entity.getIpoDate()));
			bluechip.setOkYears(entity.getOkYears());
			for(Map.Entry<Integer, String> entry : entity.getReportDates().entrySet()){
				bluechip.addReportDate(entry.getKey(), LocalDate.parse(entry.getValue()));
			}
			this.bluechips.add(bluechip);
		}
		
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
		String str = null;
		
		//List<String> codes = new ArrayList<String>();
		
		List<BluechipDto> dtos = new ArrayList<BluechipDto>();
		for(Bluechip bluechip : this.bluechips){
			isgood = false;
			
			
			if(date.isAfter(bluechip.getIpoDate())){
				//判断是否发布年报
				if(bluechip.hasReported(date)){
					if(bluechip.isOk(year) || bluechip.isOk(year-1) || bluechip.isOk(year-2)){
					//if(bluechip.isOk(year) || (bluechip.isOk(year-1) && bluechip.isOk(year-2))){
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
					if(bluechip.isOk(year-1) || bluechip.isOk(year-2) || bluechip.isOk(year-3)){
					//if(bluechip.isOk(year-1) || (bluechip.isOk(year-2) && bluechip.isOk(year-3))){
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
		dto.setIpoDate(bluechip.getIpoDate().toString());
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
		List<BluechipDto> bluechips = this.getBluechipDtos(date);
		for(BluechipDto bluechipDto : bluechips){
			BluechipView view = new BluechipView();
			view.setCode(bluechipDto.getCode());
			view.setName(bluechipDto.getName());
			view.setDate(date.toString());
			
			tradeRecordDto = tradeRecordService.getTradeRecordsDTO(bluechipDto.getCode());
			view.setUpProbability(tradeRecordDto.getTradeRecordEntity(date).getUpProbability());
			
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
}
