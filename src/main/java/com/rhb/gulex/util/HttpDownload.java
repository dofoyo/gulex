package com.rhb.gulex.util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownload {
	private static int BUFFER_SIZE = 8096;

	public static void saveToFile(String destUrl, String file){
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
		
			URL url = new URL(destUrl);
			HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
			httpUrl.setConnectTimeout(10000);
			httpUrl.setReadTimeout(10000);
			httpUrl.connect();
			BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
			
			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];
			int i = 1;
			while((size=bis.read(buf)) != -1){
				fos.write(buf, 0, size);
			}
			fos.close();
			bis.close();
			httpUrl.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
