package com.rhb.gulex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rhb.gulex.bluechip.service.BluechipService;
import com.rhb.gulex.financialstatement.spider.DownloadFinancialStatements;
import com.rhb.gulex.pb.repository.PbRepository;
import com.rhb.gulex.reportdate.repository.ReportDateRepository;
import com.rhb.gulex.reportdate.spider.DownloadReportedStockList;
import com.rhb.gulex.simulation.service.BluechipDto;
import com.rhb.gulex.simulation.service.SimulationService;
import com.rhb.gulex.stock.service.StockService;
import com.rhb.gulex.stock.spider.DownloadStockList;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;
import com.rhb.gulex.traderecord.service.TradeRecordService;
import com.rhb.gulex.traderecord.spider.DownloadTradeRecord;

@Component
public class ScheduledTask {
	/**
     * 根据cron表达式格式触发定时任务
     *  cron表达式格式:
     *      1.Seconds Minutes Hours DayofMonth Month DayofWeek Year
     *      2.Seconds Minutes Hours DayofMonth Month DayofWeek 
     *  顺序:
     *      秒（0~59）
     *      分钟（0~59）
     *      小时（0~23）
     *      天（月）（0~31，但是你需要考虑你月的天数）
     *      月（0~11）
     *      天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     *      年份（1970－2099）
     * 
     *  注:其中每个元素可以是一个值(如6),一个连续区间(9-12),一个间隔时间(8-18/4)(/表示每隔4小时),一个列表(1,3,5),通配符。
     *  由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置?.
     *  
     *  
     */
//     *  * 第一位，表示秒，取值0-59
//		* 第二位，表示分，取值0-59
//		* 第三位，表示小时，取值0-23
//		* 第四位，日期天/日，取值1-31
//		* 第五位，日期月份，取值1-12
//		* 第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思
//		          另外：1表示星期天，2表示星期一。
//		* 第7为，年份，可以留空，取值1970-2099
//		*
// 		(*)星号：可以理解为每的意思，每秒，每分，每天，每月，每年...
// 		(?)问号：问号只能出现在日期和星期这两个位置，表示这个位置的值不确定，每天3点执行，所以第六位星期的位置，我们是不需要关注的，就是不确定的值。同时：日期和星期是两个相互排斥的元素，通过问号来表明不指定值。比如，1月10日，比如是星期1，如果在星期的位置是另指定星期二，就前后冲突矛盾了。
// 		(-)减号：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12
// 		(,)逗号：表达一个列表值，如在星期字段中使用“1,2,4”，则表示星期一，星期二，星期四
// 		(/)斜杠：如：x/y，x是开始值，y是步长，比如在第一位（秒） 0/15就是，从0秒开始，每15秒，最后就是0，15，30，45，60    另：*/y，等同于0/y
	
	@Autowired
	@Qualifier("DownloadStockListFromEastmoney")
	DownloadStockList downloadStocklist;
	
	@Autowired
	@Qualifier("StockServiceImp")
	StockService stockService;
	
	@Autowired
	@Qualifier("DownloadFinancialStatementsFromSina")
	DownloadFinancialStatements downloadFinancialStatements;
	
	@Autowired
	@Qualifier("DownloadReportedStockListFromSina")
	DownloadReportedStockList downloadReportedStockList;
	
	@Autowired
	@Qualifier("DownloadTradeRecordFrom163")
	DownloadTradeRecord downloadTradeDataFrom163;

	@Autowired
	@Qualifier("DownloadTradeRecordFromQt")
	DownloadTradeRecord downloadTradeDataFromQt;

	@Autowired
	@Qualifier("TradeRecordRepositoryFromQt")
	TradeRecordRepository tradeRecordRepository;
	
	@Autowired
	@Qualifier("BluechipServiceImp")
	BluechipService bluechipService;
	
	@Autowired
	@Qualifier("ReportDateRepositoryImp")
	ReportDateRepository reportDateRepository;
	
	@Autowired
	@Qualifier("SimulationServiceImp")
	SimulationService simulationService;
	
	@Autowired
	@Qualifier("TradeRecordServiceImp")
	TradeRecordService tradeRecordService;

	
	@Autowired
	@Qualifier("PbRepositoryImp")
	PbRepository pbRepository;
	
	
	//@Scheduled(cron="0 0 5 ? * *") //每日凌晨5点，下载最新年报
	//@Scheduled(cron="0 0 12 ? * *") //临时
	public void getNewReport(){
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear() - 1;
		if(month<6){
			System.out.println(LocalDateTime.now() +  "   " + Thread.currentThread().getName() + ":  下载年报任务开始.............");
			Map<String,String> codes = downloadReportedStockList.go(Integer.toString(year));
			
	    	Map<String,String> downloadUrls = new HashMap<String,String>();
	    	Map<String,String> downloadCodes = new HashMap<String,String>();
			for(Map.Entry<String, String> entry : codes.entrySet()){
				if(reportDateRepository.getReportDate(entry.getKey(), year) == null){
					downloadUrls.put(entry.getKey()+"_balancesheet.xls",downloadFinancialStatements.downloadBalanceSheetUrl(entry.getKey()));
					downloadUrls.put(entry.getKey()+"_cashflow.xls",downloadFinancialStatements.downloadCashFlowUrl(entry.getKey()));
					downloadUrls.put(entry.getKey()+"_profitstatement.xls",downloadFinancialStatements.downloadProfitStatementUrl(entry.getKey()));
					
					downloadCodes.put(entry.getKey(),entry.getValue());
				}
			}
			
	    	//开始下载年报
			downloadFinancialStatements.down(downloadUrls);
			
			reportDateRepository.saveReportDates(downloadCodes, year);
			
			//完成年报下载后，刷新内存中STOCK对象
			//stockService.setFinancialStatements(codes);
			
			System.out.println(Thread.currentThread().getName() + ":  下载年报任务完成.............");
		}

	}

	
	//@Scheduled(cron="0 0 9 ? * 1-5") //每星期一至五上午9点，下载最新的股票清单，看有无新股
	//@Scheduled(cron="0 20 12 ? * *") //临时
	public void getNewStock(){
		System.out.println(LocalDateTime.now() +  "  " + Thread.currentThread().getName() + ":  下载新股任务开始.............");
		
		String out = "000527,600840,002710,600631,000522,601206,600005";

		
		//生成新股清单
		Map<String,String> newlist = new HashMap<String,String>();
		Map<String,String> stocklist = downloadStocklist.go();
		
		for(Map.Entry<String, String> entry : stocklist.entrySet()){
			if(!stockService.isExist(entry.getKey()) && !out.contains(entry.getKey())){
				newlist.put(entry.getKey(), entry.getValue());
				System.out.println(entry.getKey() + "," + entry.getValue());
			}
		}
		
		System.out.println("There are " + newlist.size() + " 只新股");

		
		//根据新股清单生成年报下载地址
    	Map<String,String> downloadUrls = new HashMap<String,String>();
		for(Map.Entry<String, String> entry : newlist.entrySet()){
			downloadUrls.put(entry.getKey()+"_balancesheet.xls",downloadFinancialStatements.downloadBalanceSheetUrl(entry.getKey()));
			downloadUrls.put(entry.getKey()+"_cashflow.xls",downloadFinancialStatements.downloadCashFlowUrl(entry.getKey()));
			downloadUrls.put(entry.getKey()+"_profitstatement.xls",downloadFinancialStatements.downloadProfitStatementUrl(entry.getKey()));
		}
	    		
    	//开始下载年报
		downloadFinancialStatements.down(downloadUrls);

		//？？？？？新上市公司的年报发布日期是什么时候？？？？？？
		
		//创建域对象
		stockService.creates(newlist);
		
					
		System.out.println("*********** 下载新股任务完成！  *******************");

	}
	

	//@Scheduled(cron="0 10 9 ? * 1-5") //每星期一至五上午9点10分，根据新股和新年报，重新生成blueship
	//@Scheduled(cron="0 30 12 ? * 1-6")
	public void generateBluechip() {
		bluechipService.generateBluechip();
		//bluechipService.init();
	}


	
	//@Scheduled(cron="0 30 16 ? * 1-5")  //正式，每周1至5收盘后，16:30点，下载交易数据
	//@Scheduled(cron="0 40 12 ? * *")  //临时
	public void getTradeDataFrom163(){
		System.out.println(LocalDateTime.now() +  "   " + Thread.currentThread().getName() + ":  下载交易数据任务开始.............");

		List<BluechipDto> dtos = bluechipService.getBluechips();
		
		BluechipDto szzs = new BluechipDto(); //加入上证指数
		szzs.setCode("sh000001");
		dtos.add(szzs);
		
		int i=0;
		for(BluechipDto dto : dtos){
			
			System.out.print(i++ + "/" + dtos.size() + "\r");

			downloadTradeDataFrom163.go(dto.getCode());

			//完成数据下载后，完善数据，如：av120, aboveAv120Days, bias等
			//stockService.setMarketInfo(code);		
		}

		System.out.print("下载了" + dtos.size() + "只股票的交易数据");
		
		System.out.println(Thread.currentThread().getName() + ":  下载交易数据任务结束.............");

	}
	
	@Scheduled(cron="0 30/30 9-15 ? * 1-5")  //每周1-5，9-15点，交易时间，每30分钟，执行一次模拟
	public void simulate() {   
		
		System.out.println("simulate begin .......");
		
		//下载最新的市场行情
		this.getTradeDataFromQT();
			
		//执行模拟
		simulationService.trade(LocalDate.now());
			
		System.out.println("........simulate end!");
		
	}
	
	//下载最新的市场行情
	public void getTradeDataFromQT() {   
		System.out.println("getTradeDataFromQT begin .......");
		
		String code = "sh000001";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		Map<String,String> tradeData = downloadTradeDataFromQt.go(code);  //以上证指数为依据，判断交易日，永不停牌
		LocalDate date = LocalDate.parse(tradeData.get("date"), formatter);

		LocalDate now = LocalDate.now();
		
		System.out.println("the day is " + tradeData.get("date"));
		System.out.println("today is " + now.toString());
		
		
		boolean isTradeDay = date.equals(now) ? true : false;

		if(isTradeDay) {
			System.out.println("it is a trade day!");
			
			List<Map<String,String>> records = new ArrayList<Map<String,String>>();
			Map<String,String> record = new HashMap<String,String>();
			record.put("date", now.toString());
			record.put("code", code);
			record.put("price", tradeData.get("price"));
			record.put("quantity", tradeData.get("quantity"));
			records.add(record);

			BigDecimal price;
			Integer quantity;
			
			//List<BluechipDto> bluechips = bluechipService.getBluechips(now);
			List<BluechipDto> bluechips = bluechipService.getBluechips();
			
			for(BluechipDto dto : bluechips) {
				tradeData = downloadTradeDataFromQt.go(dto.getCode());
				date = LocalDate.parse(tradeData.get("date"), formatter);
				price = new BigDecimal(tradeData.get("price"));
				quantity = new Integer(tradeData.get("quantity"));
				if(date.equals(now) && quantity>0) {  //判断是否停盘
					
					tradeRecordService.setTradeRecordEntity(dto.getCode(), now, price);
					
					record = new HashMap<String,String>();
					record.put("date", now.toString());
					record.put("code", dto.getCode());
					record.put("price", price.toString());
					record.put("quantity",tradeData.get("quantity"));
					records.add(record);
				}
			}
			
			tradeRecordRepository.save(records);
		}else {
			System.out.println("it is NOT a trade day!");

		}
		System.out.println("........getTradeDataFromQT end!");
		
	}
	
	@Scheduled(cron="0 15 9,15 ? * 1-5") //每周一至周五，上午9：15和下午15：15，执行一次
	//@Scheduled(cron="0 55 23 ? * 1-5") //每周一至周五，下午15：05，执行一次
	public void doAll() {
		this.getNewReport();
		
		this.getNewStock();
		
		this.generateBluechip();
		
		this.getTradeDataFromQT();
		
		pbRepository.download();
		
    	tradeRecordService.refresh();

    	reportDateRepository.init();
    	
    	stockService.init();
    	
		bluechipService.init();
		
		simulationService.init();	
	}

}
