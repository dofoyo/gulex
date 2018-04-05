package com.rhb.gulex.simulation.service;

import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.simulation.api.OnHandView;
import com.rhb.gulex.simulation.api.TradeRecordView;
import com.rhb.gulex.simulation.api.TradeRecordViewPlus;
import com.rhb.gulex.simulation.api.ValueView;

public interface SimulationService {
	public void init();  //要设定服务器启动后自动运行
	public void trade(LocalDate date);
	public List<TradeRecordView> getTradeRecordViews();
	public List<TradeRecordViewPlus> getTradeRecordViewPlus();
	public List<OnHandView> getOnHandViews();
	public List<ValueView> getDayValueViews();
	public List<ValueView> getYearValueViews();
}
