package com.rhb.gulex.parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rhb.gulex.util.FileUtil;
import com.rhb.gulex.util.ParseString;


/**
 * 
 * @author ran
 * 相比ParseStocklistFromEastmoney, 
 * 好处是：同花顺排除了已退市的公司
 * 缺点是：要去打开同花顺软件，手动导出股票列表
 * 
 *
 *
 *
 */
public class ParseStocklistFromTHS implements ParseStocklist {

	public Map<String,String> parse() {
		String path = "d:\\stocks\\table.xls";
		Map<String,String> stocks = new HashMap<String,String>();
		
		try {
			String stockid, stockname;
			InputStream is = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = reader.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				if(line.startsWith("SH") || line.startsWith("SZ")){
					stockid = line.substring(2,8);
					stockname = line.substring(9).trim();
					stocks.put(stockid, stockname);
				}
				//buffer.append(line); // 将读到的内容添加到 buffer 中
				//buffer.append("\n"); // 添加换行符
				line = reader.readLine(); // 读取下一行
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stocks;
	}
}
