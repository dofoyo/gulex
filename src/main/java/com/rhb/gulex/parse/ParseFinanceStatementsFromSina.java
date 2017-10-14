package com.rhb.gulex.parse;

import java.util.Map;
import java.util.TreeMap;

import com.rhb.gulex.domain.BalanceSheet;
import com.rhb.gulex.domain.CashFlow;
import com.rhb.gulex.domain.ProfitStatement;
import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;

public class ParseFinanceStatementsFromSina implements ParseFinanceStatements {

	private String pathAndFilename = "d:\\stocks\\";
	
	public Map<String,BalanceSheet> parseBalanceSheet(String stockid) {
		Map<String,BalanceSheet> balancesheets = new TreeMap<String,BalanceSheet>();
		
		String pf = pathAndFilename + stockid + "_balancesheet.xls";
		
		if(!FileUtil.isExists(pf)) return balancesheets;
		
		String str = FileUtil.readTextFile(pf);
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
		
		/*
		for(int m=0; m<j; m++){
			for(int n=0; n<i; n++){
				System.out.println("cell["+m+"]["+n+"]=" + cells[m][n]);
			}
		}
		*/
		
		for(int m=1; m<j; m++){
			String period = cells[m][0];
			if(period.contains("1231")){
				BalanceSheet bs = new BalanceSheet();
				bs.setPeriod(cells[m][0]);
				if(lines.length>100){
					bs.setCash(ParseString.toDouble(cells[m][3]));
					bs.setInventories(ParseString.toDouble(cells[m][22]));
					bs.setAccountsReceivable(ParseString.toDouble(cells[m][9]));
					bs.setNotesReceivable(ParseString.toDouble(cells[m][8]));
					bs.setPayables(ParseString.toDouble(cells[m][10]));
					bs.setAssets(ParseString.toDouble(cells[m][55]));
					bs.setDebt(ParseString.toDouble(cells[m][98]));
				}else if(lines.length > 85){   // 银行
					bs.setCash(ParseString.toDouble(cells[m][3]));
					bs.setInventories(0.0);
					bs.setAccountsReceivable(0.0);
					bs.setNotesReceivable(0.0);
					bs.setPayables(0.0);
					bs.setAssets(ParseString.toDouble(cells[m][41]));
					bs.setDebt(ParseString.toDouble(cells[m][71]));
				}else{ // 券商
					bs.setCash(ParseString.toDouble(cells[m][3]));
					bs.setInventories(0.0);
					bs.setAccountsReceivable(0.0);
					bs.setNotesReceivable(0.0);
					bs.setPayables(0.0);
					bs.setAssets(ParseString.toDouble(cells[m][24]));
					bs.setDebt(ParseString.toDouble(cells[m][44]));
				}
				balancesheets.put(period,bs);
			}
		}
		
/*		for(Map.Entry<String, BalanceSheet> entry : balancesheets.entrySet()){
			System.out.println(entry.getValue());
		}
*/		
		return balancesheets;
		
	}
	
	public Map<String,CashFlow> parseCashFlow(String stockid) {
		Map<String,CashFlow> cashflows = new TreeMap<String,CashFlow>();
		
		String pf = pathAndFilename + stockid + "_cashflow.xls";
		if(!FileUtil.isExists(pf)) return cashflows;
		
		String str = FileUtil.readTextFile(pf);
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
		
		/*
		for(int m=0; m<j; m++){
			for(int n=0; n<i; n++){
				System.out.println("cell["+m+"]["+n+"]=" + cells[m][n]);
			}
		}
		*/
		
		for(int m=1; m<j; m++){
			String period = cells[m][0];
			if(period.contains("1231")){
				CashFlow fs = new CashFlow();
				fs.setPeriod(period);
				if(lines.length>90){
					fs.setPurchaseAssets(ParseString.toDouble(cells[m][36]));
					fs.setNetCashFlow(ParseString.toDouble(cells[m][27]));
					fs.setDepreciationAssets(ParseString.toDouble(cells[m][66]) + ParseString.toDouble(cells[m][67]) + ParseString.toDouble(cells[m][68]));					
				}else if(lines.length > 76){  //银行
					fs.setPurchaseAssets(ParseString.toDouble(cells[m][20]));
					fs.setNetCashFlow(ParseString.toDouble(cells[m][12]));
					fs.setDepreciationAssets(ParseString.toDouble(cells[m][48]));					
				}else{  //券商
					fs.setPurchaseAssets(ParseString.toDouble(cells[m][23]));
					fs.setNetCashFlow(ParseString.toDouble(cells[m][15]));
					fs.setDepreciationAssets(ParseString.toDouble(cells[m][47]));					
				}
				cashflows.put(period,fs);
			}
		}

/*		for(Map.Entry<String, CashFlow> entry : cashflows.entrySet()){
			System.out.println(entry.getValue());
		}
*/		
		return cashflows;
		
	}

	public Map<String, ProfitStatement> parseProfitStatement(String stockid) {
		Map<String,ProfitStatement> profitstatements = new TreeMap<String,ProfitStatement>();
		
		String pf = pathAndFilename + stockid + "_profitstatement.xls";
		if(!FileUtil.isExists(pf)) return profitstatements;
		
		String str = FileUtil.readTextFile(pf);
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
		
		/*
		for(int m=0; m<j; m++){
			for(int n=0; n<i; n++){
				System.out.println("cell["+m+"]["+n+"]=" + cells[m][n]);
			}
		}
		*/
		
		for(int m=1; m<j; m++){
			String period = cells[m][0];
			if(period.contains("1231")){
				ProfitStatement fs = new ProfitStatement();
				fs.setPeriod(period);
				if(lines.length > 50){
					fs.setAllOperatingRevenue(ParseString.toDouble(cells[m][2]));
					fs.setOperatingRevenue(ParseString.toDouble(cells[m][3]));
					fs.setAllOperatingCost(ParseString.toDouble(cells[m][9]));
					fs.setOperatingCost(ParseString.toDouble(cells[m][10]));
					fs.setOperatingExpense(ParseString.toDouble(cells[m][23]));
					fs.setFinanceExpense(ParseString.toDouble(cells[m][24]));
					fs.setSalesExpense(ParseString.toDouble(cells[m][22]));
					fs.setTax(ParseString.toDouble(cells[m][39]));
				}else if(lines.length > 35){ //券商
					fs.setAllOperatingRevenue(ParseString.toDouble(cells[m][2]));
					fs.setOperatingRevenue(ParseString.toDouble(cells[m][2]));
					fs.setAllOperatingCost(ParseString.toDouble(cells[m][15]));
					fs.setOperatingCost(0.00);
					fs.setOperatingExpense(0.00);
					fs.setFinanceExpense(0.00);
					fs.setSalesExpense(0.00);
					fs.setTax(ParseString.toDouble(cells[m][24]));
				}else{	//银行
					fs.setAllOperatingRevenue(ParseString.toDouble(cells[m][2]));
					fs.setOperatingRevenue(ParseString.toDouble(cells[m][3]));
					fs.setAllOperatingCost(ParseString.toDouble(cells[m][4]));
					fs.setOperatingCost(ParseString.toDouble(cells[m][5]));
					fs.setOperatingExpense(ParseString.toDouble(cells[m][8]));
					fs.setFinanceExpense(ParseString.toDouble(cells[m][9]));
					fs.setSalesExpense(ParseString.toDouble(cells[m][7]));
					fs.setTax(ParseString.toDouble(cells[m][20]));
				}
				profitstatements.put(period,fs);
			}
		}
		
/*		for(Map.Entry<String, ProfitStatement> entry : profitstatements.entrySet()){
			System.out.println(entry.getValue());
		}
*/		
		return profitstatements;
		
	}

}
