package com.rhb.gulex.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rhb.gulex.repository.reportdate.ReportDateEntity;

public class ParseStringTest {

	
	
	
	@Test
	public void test1(){
		String path = "D:/workspace/gulex-data/fina/sina/";
	    File[] files = new File(path).listFiles();
		
		//List<File> files  = FileUtil.getFiles(path, ".xls", true);
		for(File file : files){
			System.out.println(file.getName());
		}

	}
	
	
	
	
	
	
	
	//@Test
	public void test(){
		String url = "http://finance.sina.com.cn/realstock/income_statement/2016-12-31/issued_pdate_de_5.html";
		String result = HttpDownload.getResult(url);
		List<String> trs = getTrs1(result);
		trs.addAll(getTrs2(result));
		for(String tr : trs){
			List<String> tds = getTds(tr);
			String code = getCode(tds.get(0));
			String date = tds.get(2);
			System.out.println(code + "," + date);
		}
	}

	private List<String> getTds(String str){
		String regexp = "<td>|</td>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list;
	}
	
	private List<String> getTrs2(String str){
		String regexp = "<tr style='background:#F1F6FC;'>|</tr>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list;
	}

	
	private List<String> getTrs1(String str){
		String regexp = "<tr>|</tr>";
		List<String> list = ParseString.subStrings(str,regexp);
		return list;
	}

	
	private String getCode(String str){
		String regexp = "nc.shtml\">|</a>";
		String code = ParseString.subString(str,regexp);
		return code;
	}
	

	
}
