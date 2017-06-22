package com.rhb.gulex.parse;

import java.util.Map;

import com.rhb.gulex.domain.BalanceSheet;
import com.rhb.gulex.domain.CashFlow;
import com.rhb.gulex.domain.ProfitStatement;

public interface ParseFinanceStatements {

	public Map<String,BalanceSheet> parseBalanceSheet(String stockid);
	public Map<String,CashFlow> parseCashFlow(String stockid);
	public Map<String,ProfitStatement> parseProfitStatement(String stockid);
}
