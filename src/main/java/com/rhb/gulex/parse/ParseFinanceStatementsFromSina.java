package com.rhb.gulex.parse;

import java.util.Map;
import java.util.TreeMap;

import com.rhb.gulex.domain.BalanceSheet;
import com.rhb.gulex.domain.CashFlow;
import com.rhb.gulex.domain.ProfitStatement;
import com.rhb.gulex.util.FileUtil;

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
				if(lines.length>98){
					bs.setPeriod(cells[m][0]);
					bs.setCash(Double.valueOf(cells[m][3]));
					bs.setInventories(Double.valueOf(cells[m][22]));
					bs.setAccountsReceivable(Double.valueOf(cells[m][9]));
					bs.setNotesReceivable(Double.valueOf(cells[m][8]));
					bs.setPayables(Double.valueOf(cells[m][10]));
					bs.setAssets(Double.valueOf(cells[m][55]));
					bs.setDebt(Double.valueOf(cells[m][98]));
					balancesheets.put(period,bs);
				}else{
					bs.setPeriod(cells[m][0]);
					bs.setCash(Double.valueOf(cells[m][3]));
					bs.setInventories(0.0);
					bs.setAccountsReceivable(0.0);
					bs.setNotesReceivable(0.0);
					bs.setPayables(0.0);
					bs.setAssets(Double.valueOf(cells[m][46]));
					bs.setDebt(Double.valueOf(cells[m][80]));
					balancesheets.put(period,bs);
				}
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
				if(lines.length>68){
					fs.setPeriod(period);
					fs.setPurchaseAssets(Double.valueOf(cells[m][36]));
					fs.setNetCashFlow(Double.valueOf(cells[m][27]));
					fs.setDepreciationAssets(Double.valueOf(cells[m][66]) + Double.valueOf(cells[m][67]) + Double.valueOf(cells[m][68]));					
				}else{
					fs.setPeriod(period);
					fs.setPurchaseAssets(Double.valueOf(cells[m][36]));
					fs.setNetCashFlow(Double.valueOf(cells[m][27]));
					fs.setDepreciationAssets(0.0);					
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
				fs.setAllOperatingRevenue(Double.valueOf(cells[m][2]));
				fs.setOperatingRevenue(Double.valueOf(cells[m][3]));
				fs.setAllOperatingCost(Double.valueOf(cells[m][9]));
				fs.setOperatingCost(Double.valueOf(cells[m][10]));
				fs.setOperatingExpense(Double.valueOf(cells[m][23]));
				fs.setFinanceExpense(Double.valueOf(cells[m][24]));
				fs.setSalesExpense(Double.valueOf(cells[m][22]));
				fs.setTax(Double.valueOf(cells[m][39]));
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
