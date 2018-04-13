package com.rhb.gulex.pb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.gulex.pb.repository.PbRepository;
import com.rhb.gulex.pb.spider.PbEntity;


@Service("PbServiceImp")
public class PbServiceImp implements PbService {
	@Autowired
	@Qualifier("PbRepositoryImp")
	PbRepository pbRepository;
	
	private Map<String,String> hsagPbsMap = null;
	private List<String> hsagPbsList = null;
	
	@Override
	public List<Map<String, String>> getHsagPbs() {
		List<Map<String,String>> list = new ArrayList();
		List<PbEntity> entities = pbRepository.getPbEntities();
		
		for(PbEntity entity : entities) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("date", entity.getDate());
			map.put("hsag", entity.getHsag());
			list.add(map);
		}
		return list;
	}
	
	

	@Override
	public BigDecimal getHsagRate(LocalDate date) {
		if(hsagPbsMap == null) {
			this.init();
		}
		
		String hsag = hsagPbsMap.get(date.toString());
		for(int i=0; i<hsagPbsMap.size() && hsag==null; i++) {
			date = date.minusDays(1);
			 hsag = hsagPbsMap.get(date.toString());
		}
		
		//System.out.println(date + "'s hsag = " + hsag);
		BigDecimal location;
		if(hsagPbsList.contains(hsag)) {
			location = new BigDecimal(hsagPbsList.indexOf(hsag));
		}else {
			location = new BigDecimal(hsagPbsList.size()-1);
		}
		
		
		//System.out.println("location = " + location);
		//Integer rate = location.divide(new BigDecimal(hsagPbsList.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		
		return location.divide(new BigDecimal(hsagPbsList.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
	}

	@Override
	public void init() {
		hsagPbsMap = new HashMap<String,String>();
		Set<String> set = new HashSet<String>();

		List<PbEntity> entities = pbRepository.getPbEntities();
		for(PbEntity entity : entities) {
			hsagPbsMap.put(entity.getDate(), entity.getHsag());
			set.add(entity.getHsag());
		}
		
		hsagPbsList = new ArrayList<String>(set);
		Collections.sort(hsagPbsList,new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				BigDecimal b1 = new BigDecimal(o1);
				BigDecimal b2 = new BigDecimal(o2);
				return b1.compareTo(b2);
			}
			
		});
		
/*		
		for(Map.Entry<String, String> entry : hsagPbsMap.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		
		for(String str : hsagPbsList) {
			System.out.println(str);
		}*/
	}



	@Override
	public Integer getBuyValve(LocalDate date) {
		BigDecimal coefficient = new BigDecimal(2.8);
		Integer valve =this.getHsagRate(date).multiply(coefficient).intValue();
		
		return valve>85 ? 85 : (valve<50 ? 50 : valve);
	}
	
	/*when coefficient=2.8,result is max.
	 * 2010-01-01
	 * 3.5   4774319.00
	 * 3.0   5629025.00
	 * 2.8   5669722.00
	 * 2.5   5487112.00
	 * 
	 * 2011-05-03
	 * 3.0   5561658.00
	 * 2.8   5569958.00
	 * 2.5   5371793.00
	 * 
	 */

}
