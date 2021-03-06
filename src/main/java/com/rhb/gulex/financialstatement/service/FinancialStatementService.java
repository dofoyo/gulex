package com.rhb.gulex.financialstatement.service;

import java.util.List;
import java.util.Map;

public interface FinancialStatementService {
	public boolean isOk(String stockcode, Integer year);
	public Map<Integer,String> getOks(String stockcode);
	public List<OkfinanceStatementDto> getOks();
	
	public List<Integer> getPeriods(String stockcode);
	
}
