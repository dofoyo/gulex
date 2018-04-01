package com.rhb.gulex.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class HttpDownFileTest {

	//@Test
	public void downloadBalanceSheet(){
		String stockNo = "601398";

		String destUrl = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/"+stockNo+"/ctrl/all.phtml";
		System.out.println(destUrl);
		String fileName = "C:\\" + stockNo + ".xls";
		HttpDownload.saveToFile(destUrl, fileName);
		System.out.println("test done!");

	}
	
	
	//@Test
	public void testPing(){
		Runtime runtime =Runtime.getRuntime(); // 获取当前程序的运行进对象
		  Process process = null; //声明处理类对象
		  String line = null; //返回行信息
		  InputStream is = null; //输入流
		  InputStreamReader isr = null;// 字节流
		  BufferedReader br = null;
		  String ip = "www.baidu.com -t";
		  //boolean res = false;// 结果

		  String file = "d:\\ping.csv";
		   String regexp = "时间=|ms TTL";
		   //String pingvalue = null;
		   StringBuffer sb = new StringBuffer();
		  
		   try {
			   process =runtime.exec("ping " + ip); // PING
			   is =process.getInputStream(); // 实例化输入流
			   isr = new InputStreamReader(is,"gbk");// 把输入流转换成字节流
			   br = new BufferedReader(isr);// 从字节中读取文本
			   String ping;
			   
			   int i=0;
			   while ((line= br.readLine()) != null) {
				    ping = ParseString.subString(line, regexp);
				    System.out.println(this.getDatetime() + "," + (isNumeric(ping) ? ping : "100000"));
					sb.append(this.getDatetime() + "," + ParseString.subString(line, regexp) + "\n");
				    if(i++ > 10) {
				    	System.out.println("save...");
				    	FileUtil.writeTextFile(file, sb.toString(), true);
				    	sb = new StringBuffer();
				    	i=0;
				    }
			   }
			   is.close();
			   isr.close();
			    br.close();
		  } catch (IOException e) {
			  runtime.exit(1);
		  }finally{
			  //FileUtil.writeTextFile(file, sb.toString(), true);
		  }
	}
	
	private String getDatetime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,yyyy-MM-dd HH-mm,yyyy-MM-dd HH,yyyy-MM-dd");
        return sdf.format(new Date());
	}
	
    private boolean isNumeric(String str){
    	if(str == null){
    		return false;
    	}
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
 }
	
}
