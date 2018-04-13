package com.rhb.gulex.simulation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.simulation.api.OnHandView;
import com.rhb.gulex.simulation.api.SimulationView;
import com.rhb.gulex.simulation.api.SimulationViewPlus;
import com.rhb.gulex.simulation.api.ValueView;

public interface SimulationService {
	public void init();  //要设定服务器启动后自动运行
	
	public void trade(LocalDate date);
	
	public List<SimulationView> getTradeRecordViews();
	
	public List<SimulationViewPlus> getTradeRecordViewPlus();
	
	public List<OnHandView> getOnHandViews();
	
	public List<ValueView> getDayValueViews();
	
	public List<ValueView> getYearValueViews();
	
	
	public void setBuyLinePeriod(Integer count);
	public void setAutoBuyValve(boolean autoBuyLine);
	
	public void setBeginDate(LocalDate beginDate);
	
	public BigDecimal getTotal();
	
	public void setStopLoss(boolean stopLoss);

	public void setStopLossRate(Integer stopLossRate);

	public void setOnHandsLimit(boolean onHandsLimit);

	public void setOnHandsLimitNumber(Integer onHandsLimitNumber);
 
	public void setFinancing(boolean financing) ;
	
	public String getSettingString();

	
}
