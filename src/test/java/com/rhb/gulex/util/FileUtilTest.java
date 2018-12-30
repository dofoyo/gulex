package com.rhb.gulex.util;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class FileUtilTest {

	//@Test
	public void testGetFiles(){
		String path = "D:\\stocks";
		String suffix = "xls";
		List<File> files = FileUtil.getFiles(path, suffix, true);
		System.out.println(files.size());
	}
	
	//@Test
	public void test(){
		String path = "D:\\stocks\\table.xls";
		String str = FileUtil.readTextFile(path);
		System.out.println(str);
	}
	
	
	@Test
	public void testToFile(){
		String fileName;
		String str;
		StringBuffer sb = new StringBuffer();
		String regexp = "<article class=\"article-content\">|<div class=\"pagination2\">";
		for(int i=1; i<=134; i++) {
			System.out.println(i);
			fileName = "d:/mydocs/rhb/ccll/"+i+".html";
			
			str = FileUtil.readTextFile(fileName);
			str = str.replaceAll("\r", "");
			str = str.replaceAll("\n", "");
			
			sb.append(ParseString.subString(str, regexp));
			
				
		}
		
		FileUtil.writeTextFile("d:/mydocs/rhb/ccll/ccll.html", sb.toString(), true);
		
		//System.out.println(sb.toString());

		System.out.println("test done!");

	}

}
