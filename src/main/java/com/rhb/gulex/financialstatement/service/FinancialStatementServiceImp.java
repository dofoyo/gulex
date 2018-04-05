package com.rhb.gulex.financialstatement.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.gulex.financialstatement.repository.FinanceStatementsRepository;
import com.rhb.gulex.reportdate.repository.ReportDateRepository;

@Service("FinancialStatementServiceImp")
public class FinancialStatementServiceImp implements FinancialStatementService {
	@Value("${dataPath}")
	private String dataPath;

	@Autowired
	@Qualifier("FinanceStatementsRepositoryFromSina")
	FinanceStatementsRepository financeStatementsRepository;

	@Autowired
	@Qualifier("ReportDateRepositoryImp")
	ReportDateRepository reportDateRepository;
	
	Map<String,FinancialStatement> financialStatements = new HashMap<String,FinancialStatement>();
	
	private String out = "000527,600840,002710,600631,000522,601206,600005";

	
	@Override
	public boolean isOk(String stockcode, Integer year) {
		if(out.indexOf(stockcode)!=-1){
			return false;
		}
		if(!financialStatements.containsKey(stockcode)){
			setFinancialStatement(stockcode);
		}
		
		boolean flag = false;
		
		FinancialStatement fs = financialStatements.get(stockcode);
		if(fs!=null){
			flag = fs.isOK(year);
		}		
		return flag;
	}
	
	@Override
	public List<Integer> getPeriods(String stockcode){
		if(!financialStatements.containsKey(stockcode)){
			setFinancialStatement(stockcode);
		}
		List<Integer> years = null;
		FinancialStatement fs = financialStatements.get(stockcode);
		if(fs!=null){
			years = fs.getPeriods();
		}
		
		return years;
		
	}
	
	
	private void setFinancialStatement(String stockcode) {
		//System.out.println("setFinancialStatement of  " + stockcode);
		FinancialStatement fs = new FinancialStatement();
		fs.setBalancesheets(financeStatementsRepository.getBalanceSheets(stockcode));
		fs.setCashflows(financeStatementsRepository.getCashFlows(stockcode));
		fs.setProfitstatements(financeStatementsRepository.getProfitStatements(stockcode));
		financialStatements.put(stockcode, fs);
		//stock.refreshFinancialStatements();

	}


	@Override
	public Map<Integer, String> getOks(String stockcode) {
		Map<Integer, String> oks  = new HashMap<Integer, String>();
		
		if(!financialStatements.containsKey(stockcode)){
			setFinancialStatement(stockcode);
		}
		
		FinancialStatement fs = financialStatements.get(stockcode);

				
		Map<Integer, String> reportdates = reportDateRepository.getReportDates(stockcode);
		for(Map.Entry<Integer, String> entry : reportdates.entrySet()){
			if(fs.isOK(entry.getKey())){
				oks.put(entry.getKey(), entry.getValue());
			}
		}
		
		return oks;
	}


	@Override
	public List<OkfinanceStatementDto> getOks() {
		List<OkfinanceStatementDto> dtos = new LinkedList<OkfinanceStatementDto>();
		List<Integer> years;
		Set<String> codes = financeStatementsRepository.getReportedStockcode();
		int i=0;
		for(String code : codes){
			System.out.print(i++ + "/" + codes.size() + " codes.size" + "\r");

			years = this.getPeriods(code);
			for(Integer year : years){
				if(this.isOk(code, year)){
					String reportDate = reportDateRepository.getReportDate(code, year);
					OkfinanceStatementDto dto = new OkfinanceStatementDto();
					dto.setStockcode(code);
					dto.setYear(year);
					dto.setReportdate(reportDate==null ? null : reportDate.toString());
					dtos.add(dto);
					//System.out.println(dto);
				}
			}
		}
		
		return dtos;
	}
	

}
