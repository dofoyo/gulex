package com.rhb.gulex.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestLineChart {

	@Test
	public void test() {
    	List<LineChartDTO> datas = new ArrayList<LineChartDTO>();
    	LineChartDTO dto1 = new LineChartDTO(100, "price", "2018-04-01");
    	LineChartDTO dto2 = new LineChartDTO(110, "price", "2018-04-02");
    	LineChartDTO dto3 = new LineChartDTO(120, "price", "2018-04-03");
    	LineChartDTO dto4 = new LineChartDTO(130, "price", "2018-04-04");
    	LineChartDTO dto5 = new LineChartDTO(140, "price", "2018-04-05");
    	LineChartDTO dto6 = new LineChartDTO(155, "av120", "2018-04-02");
    	LineChartDTO dto7 = new LineChartDTO(135, "av120", "2018-04-03");
    	LineChartDTO dto8 = new LineChartDTO(125, "av120", "2018-04-04");
    	LineChartDTO dto9 = new LineChartDTO(145, "av120", "2018-04-05");
    	LineChartDTO dto10 = new LineChartDTO(115, "av120", "2018-04-01");
    	datas.add(dto1);
    	datas.add(dto2);
    	datas.add(dto3);
    	datas.add(dto4);
    	datas.add(dto5);
    	datas.add(dto6);
    	datas.add(dto7);
    	datas.add(dto8);
    	datas.add(dto9);
    	datas.add(dto10);
    	
    	File file = new File("D:\\test.png");
    	int width = 720;
    	int hight = 480;
    	
    	int i = LineChart.draw("test", file, datas, width, hight);
    	
    	if(i==1) {
    		System.out.println("success!");
    	}
    	
	}
}
