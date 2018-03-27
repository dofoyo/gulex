package com.rhb.gulex.simulation;

import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.api.simulation.OnHandView;
import com.rhb.gulex.api.simulation.TradeRecordView;
import com.rhb.gulex.api.simulation.ValueView;

public interface SimulationService {
	public void init();  //要设定服务器启动后自动运行
	public void trade(LocalDate date);
	public List<TradeRecordView> getTradeRecordViews();
	public List<OnHandView> getOnHandViews();
	public List<ValueView> getValueViews();
}
