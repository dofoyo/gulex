package com.rhb.gulex.util;

import java.io.File;

import org.junit.Test;

public class TradeRecordRepositoryImpFromDzhTest {
	@Test
	public void test(){
		String path="D:/stocks/trade/dzh/000012.txt";
		File file = new File(path);
		if(!file.exists()){
			System.out.println(path + " do NOT exist.");
		}
		
		String str = FileUtil.readTextFile(path);
		
		String[] lines = str.split("\n");
		String line;
		String[] cells;
		for(int i=2; i<lines.length; i++){
			line = lines[i];
			cells = line.split("\t");
			String date = cells[0];
			String price = cells[4];
			System.out.println(date + "," + price);
		}
	}
}
