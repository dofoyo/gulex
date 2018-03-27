package com.rhb.gulex.service.fin;

import java.util.List;
import java.util.Map;

public interface FinancialStatementService {
	public boolean isOk(String stockcode, Integer year);
	public Map<Integer,String> getOks(String stockcode);
	public List<OkfinanceStatementDto> getOks();
}
