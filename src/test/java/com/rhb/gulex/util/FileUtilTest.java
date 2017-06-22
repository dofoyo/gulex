package com.rhb.gulex.util;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testGetFiles(){
		String path = "D:\\stocks";
		String suffix = "xls";
		List<File> files = FileUtil.getFiles(path, suffix, true);
		System.out.println(files.size());
	}
}
