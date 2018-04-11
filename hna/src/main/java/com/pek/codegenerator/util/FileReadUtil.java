package com.pek.codegenerator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FileReadUtil {
	static Logger logger = LoggerFactory.getLogger(FileReadUtil.class);
	
	public static String readFile(String fileName){
		StringBuilder jsonBuilder = new StringBuilder();
		BufferedReader reader = null;
		logger.info("*****此类只能在测试情况下调用，被请求读取的文件为[" + fileName + "]");
		String clazzPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String fliePath = clazzPath + File.separator + fileName;
		try {
//			String webRoot = System.getProperty("user.dir");
//			String filePath = webRoot + File.separator + "WEB-INF" + File.separator + "classes"+ File.separator + fileName;
			reader = new BufferedReader(new InputStreamReader( new FileInputStream(fliePath), "UTF-8"));

			String s = reader.readLine();
			while (s != null) {
				jsonBuilder.append(s).append("//r//n");
				s = reader.readLine();
			}
		} catch (IOException e) {
			logger.error("",e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("",e);
			}
			System.gc();
		}
		return jsonBuilder.toString();
	}

}
