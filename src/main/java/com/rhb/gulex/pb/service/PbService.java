package com.rhb.gulex.pb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PbService {
	public void init();

	public List<Map<String, String>> getHsagPbs();
	
	public BigDecimal getHsagRate(LocalDate date);
	
	public Integer getBuyValve(LocalDate date);
	
	public List<String> getHsagPbsList();
	
	public Integer getBuyValve(String pb);

}
