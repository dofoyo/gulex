package com.rhb.gulex.util;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart {
	public static int draw(String title, File file, List<LineChartDTO> datas) {
		int success = 1;
        try {
			ChartUtils.saveChartAsPNG(file, createLineChart(title,datas), 720, 480);
		} catch (IOException e) {
			success = -1;
			e.printStackTrace();
		}
        return success;
	}
	public static int draw(String title, File file, List<LineChartDTO> datas, int width, int hight) {
		int success = 1;
        try {
			ChartUtils.saveChartAsPNG(file, createLineChart(title,datas), width, hight);
		} catch (IOException e) {
			success = -1;
			e.printStackTrace();
		}
        return success;
	}
	
	private static JFreeChart createLineChart(String title, List<LineChartDTO> datas) {
		//创建数据集对象
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
		for(LineChartDTO dto : datas) {
			dataset.addValue(dto.getValue(), dto.getLineName(), dto.getPointName());

		}
		
		
		
		int row = dataset.getRowCount();
		int column =  dataset.getColumnCount();
		//System.out.println("(row,column) = (" + row + "," + column + ")=" + dataset.getValue(1, 2) );
		double max = 0.0;
		double min = 1000000.0;
		double value = 0.0;
		for(int i=0; i<row; i++) {
			for(int j=0; j<column; j++) {
				value = dataset.getValue(i, j).longValue();
				max = value>max ? value : max;
				min = value<min ? value : min;
			}
		}
		
		//System.out.println(max);
		//System.out.println(min);
		
		// 通过ChartFactory创建JFreeChart
		//String title = "";
		String categoryAxisLabel = "";
		String valueAxisLabel = "";
		
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        ChartFactory.setChartTheme(standardChartTheme);
        
        JFreeChart lineChart = ChartFactory.createLineChart(
        		title, 
        		categoryAxisLabel, 
        		valueAxisLabel, 
        		dataset);
        
        CategoryPlot categoryPlot = lineChart.getCategoryPlot(); 
        //ValueAxis valueAxis = categoryPlot.getRangeAxis() ;
        //valueAxis.setRange(min,max) ; 
        //categoryPlot.setRangeAxis(valueAxis);
        
        CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
        //categoryAxis.set
        
        //categoryAxis.setti
        //categoryAxis.getAttributedLabel();
        
        //categoryAxis.set
        //categoryAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY,interval));
        //categoryAxis.setVerticalTickLabels(true);

        return lineChart;
	}
	
	
    // 本地测试  
    public static void main(String[] args) {
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
    	
    	
    	ChartFrame cf = new ChartFrame("Test", createLineChart("test",datas));  
        cf.setSize(900,300);
        cf.pack();  
        cf.setVisible(true);  
    }  
}
