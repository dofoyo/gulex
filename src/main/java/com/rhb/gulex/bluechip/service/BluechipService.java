package com.rhb.gulex.bluechip.service;

import java.time.LocalDate;
import java.util.List;

import com.rhb.gulex.bluechip.api.BluechipView;
import com.rhb.gulex.simulation.service.BluechipDto;

public interface BluechipService {
	public List<BluechipDto> getBluechips();
	public BluechipDto getBluechips(String stockcode);
	public List<BluechipDto> getBluechips(LocalDate date);
	public List<BluechipView> getBluechipViews(LocalDate date);
	public boolean inGoodPeriod(String stockcode, LocalDate date);
	public void generateBluechip();
	public void init();


}
