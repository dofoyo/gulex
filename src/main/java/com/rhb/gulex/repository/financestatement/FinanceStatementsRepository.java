package com.rhb.gulex.repository.financestatement;

import java.util.Map;

import com.rhb.gulex.domain.BalanceSheet;
import com.rhb.gulex.domain.CashFlow;
import com.rhb.gulex.domain.ProfitStatement;

public interface FinanceStatementsRepository {

	public Map<String,BalanceSheet> getBalanceSheets(String stockid);
	public Map<String,CashFlow> getCashFlows(String stockid);
	public Map<String,ProfitStatement> getProfitStatements(String stockid);
}
