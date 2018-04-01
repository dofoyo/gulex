package com.rhb.gulex.financialstatement.repository;

import java.util.Map;
import java.util.Set;

import com.rhb.gulex.financialstatement.service.BalanceSheet;
import com.rhb.gulex.financialstatement.service.CashFlow;
import com.rhb.gulex.financialstatement.service.ProfitStatement;

public interface FinanceStatementsRepository {

	public Map<String,BalanceSheet> getBalanceSheets(String stockid);
	public Map<String,CashFlow> getCashFlows(String stockid);
	public Map<String,ProfitStatement> getProfitStatements(String stockid);
	
	public Set<String> getReportedStockcode();
	
}
