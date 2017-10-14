package com.rhb.gulex.parse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.rhb.gulex.domain.BalanceSheet;
import com.rhb.gulex.domain.CashFlow;
import com.rhb.gulex.domain.ProfitStatement;
import com.rhb.gulex.domain.Stock;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

public class ParseFinanceStatementsFromSinaTest {
	ParseFinanceStatements pfs = new ParseFinanceStatementsFromSina();
	private String stockid = "300580";
	
	
	//@Test
	public void parseBalanceSheetTest(){
		Map<String,BalanceSheet> balancesheets = pfs.parseBalanceSheet(stockid);
		
		for(Map.Entry<String, BalanceSheet> entry : balancesheets.entrySet()){
			System.out.println(entry.getValue());
		}
	}

	//@Test
	public void parseCashFlowTest(){
		Map<String,CashFlow> cashflows = pfs.parseCashFlow(stockid);
		
		for(Map.Entry<String, CashFlow> entry : cashflows.entrySet()){
			System.out.println(entry.getValue());
		}
	}
	
	//@Test
	public void parseProfitStatementTest(){
		Map<String,ProfitStatement> profitstatements = pfs.parseProfitStatement(stockid);
		
		for(Map.Entry<String, ProfitStatement> entry : profitstatements.entrySet()){
			System.out.println(entry.getValue());
		}
	}
	
	//@Test
	public void differentBalancesheet(){
		String pathAndFilename = "d:\\stocks\\";
		ParseStocklist psl = new ParseStocklistFromEastmoney();
		
		Map<String,String> stocks = psl.parse();
		String stockid = null;
		Set<String> diff = new HashSet<String>();
		int k = 1;
		for(Map.Entry<String, String> entry : stocks.entrySet()){
			
			stockid = entry.getKey();
			System.out.println(k++ + "/" + stocks.size());
			System.out.println("********    stock = " + entry.getValue());

			String str = FileUtil.readTextFile(pathAndFilename + stockid + "_profitstatement.xls");
			String[] lines = str.split("\n");
			String[] columns = lines[0].split("\t");
			String[][] cells = new String[columns.length][lines.length];
			
			int i = 0;
			int j = 0;
			for(String line : lines){
				columns = line.split("\t");
				j=0;
				for(String cell : columns){
					cells[j][i] = cell;
					j++;
				}
				i++;
			}
			
			StringBuffer titles = new StringBuffer();
			for(int m=0; m<i; m++){
				titles.append(cells[0][m]);
				titles.append(",");
			}
			
			diff.add(titles.toString());
			
		}
		
		for(String title : diff){
			System.out.println(title);
		}
	}
	
}
