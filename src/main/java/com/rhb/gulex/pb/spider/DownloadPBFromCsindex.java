package com.rhb.gulex.pb.spider;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhb.gulex.traderecord.repository.TradeRecordEntity;
import com.rhb.gulex.traderecord.repository.TradeRecordRepository;
import com.rhb.gulex.util.HttpDownload;
import com.rhb.gulex.util.ParseString;

@Service("DownloadPBFromCsindex")
public class DownloadPBFromCsindex implements DownloadPB {
	@Value("${dataPath}")
	private String dataPath;
	
	private String filename = "pbs.json";
	
	@Autowired
	@Qualifier("TradeRecordRepositoryImpFrom163")
	TradeRecordRepository tradeRecordRepositoryFrom163;
	
	@Override
	public void downloadAll() {
		List<PbEntity> entities = new ArrayList<PbEntity>();
		
		String beginDate = "2011-05-02";
		List<LocalDate> tradeDates = this.getTradeDate(LocalDate.parse(beginDate));
		int i=0;
		for(LocalDate date : tradeDates) {
			//System.out.print(i++ + "/" + tradeDates.size() + "\r");
			try {
				entities.add(this.download(date.toString()));
				Thread.sleep(5000);  //5秒
			} catch (InterruptedException e){} 
		}
		
		writeToFile(dataPath+filename, entities);
		
	}
	
	private void writeToFile(String jsonfile, List<PbEntity> object){
		ObjectMapper mapper = new ObjectMapper();
    	try {
			mapper.writeValue(new File(jsonfile),object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private String getPb(String str) {
		String regexp = "<td>|</td>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list.get(1);
		
	}
	
	private List<String> getTrs(String str){
		String regexp = "<tr>|</tr>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list;
	}
	
	private String getTbody(String str) {
		String regexp = "<tbody.*?>|</tbody>";
		String body = ParseString.subString(str, regexp);
		return body;
	}
	
	private List<LocalDate> getTradeDate(LocalDate beginDate){
		String code = "sh000001"; //以上证指数的交易日期作为历史交易日历
		List<LocalDate> dates = new ArrayList<LocalDate>();
		List<TradeRecordEntity> records = tradeRecordRepositoryFrom163.getTradeRecordEntities(code);
		for(TradeRecordEntity record : records){
			//System.out.print(record.getDate());
			if(record.getDate().isAfter(beginDate)){
				dates.add(record.getDate());
				//System.out.println("   y");
			}
		}
		return dates;
	}

	@Override
	public PbEntity download(String date) {
		String url = "http://www.csindex.com.cn/zh-CN/downloads/industry-price-earnings-ratio?date="+date+"&type=zy3";
		System.out.println(url);
		String result = HttpDownload.getResult(url);
		result = result.replaceAll("[\\t\\n\\r]", "").replaceAll(" ", "");
		String body = this.getTbody(result);
		
		List<String> trs = this.getTrs(body);
		PbEntity entity = new PbEntity();
		entity.setDate(date);
		entity.setShag(getPb(trs.get(0)));
		entity.setSzag(getPb(trs.get(1)));
		entity.setHsag(getPb(trs.get(2)));
		entity.setSszb(getPb(trs.get(3)));
		entity.setZxb(getPb(trs.get(4)));
		entity.setCyb(getPb(trs.get(5)));
		
		System.out.println(entity);
		
		return entity;
	}



}
