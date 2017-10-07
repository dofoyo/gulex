package com.rhb.gulex.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseString {

	public static String subString(String source, String regexp){
		List<String> list = subStrings(source,regexp);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public static List<String> subStrings(String source, String regexp){
		List list = new ArrayList();

		if(source == null || regexp==null || "".equals(regexp.trim())){
			return list;
		}
		String findRegexp = regexp.replace("|", ".*?");
		boolean replace = regexp.indexOf("|")==-1 ? false : true;
		
		Pattern pt = Pattern.compile(findRegexp);
		Matcher mt = pt.matcher(source);
		//System.out.println(mt.group());
		while (mt.find()){
			if(replace){
				list.add(mt.group().replaceAll(regexp, ""));				
			}else{
				list.add(mt.group());
			}
		}
		return list;
	}
	
	public static String subString(String source, String begin, String end){
		if(source==null || begin==null || end==null){
			return "";
		}
		int i = source.indexOf(begin);
		int j = source.indexOf(end);
		
		return source.substring(i+begin.length(), j);
		
	}
	
	public static double toDouble(String str){
		double d = 0.00;
		if(str!=null && !str.trim().isEmpty()){
			d = Double.valueOf(str);
		}
		return d;
	}

}
