package com.rhb.gulex.download;

import java.util.Map;

public interface DownloadFinancialStatements {

	public void down(Map<String,String> urls);
	
	public String downloadBalanceSheetUrl(String stockid);
	public String downloadCashFlowUrl(String stockid);
	public String downloadProfitStatementUrl(String stockid);
}
