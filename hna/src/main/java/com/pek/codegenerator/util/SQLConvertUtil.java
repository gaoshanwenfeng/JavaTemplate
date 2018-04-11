package com.pek.codegenerator.util;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLConvertUtil {
	static Logger logger = LoggerFactory.getLogger(SQLConvertUtil.class);

	/**
	 * 列装换成java代码
	 * @param fileName
	 * @param valName
	 * @return
	 */
	public static  String convertFileContentToSQLCode(String fileName,String valName){
		StringBuffer sb = new StringBuffer();
		InputStream is = SQLConvertUtil.class.getResourceAsStream(fileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String lineVal = null;
			lineVal = br.readLine();
			while (lineVal != null) {
				sb.append(valName).append(".append(\" ").append(lineVal.trim().toUpperCase()).append(" \");\n");
				lineVal = br.readLine();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return sb.toString();
	}
	
	/**
	 * 列转换成SQL语句中In
	 * @param fileName
	 */
	public static String mutilyLineForIn(String fileName){
		StringBuffer sb = new StringBuffer();
		try {
			String value =  FileReadUtil.readFile(fileName);//SQLConvertUtil.class.getResourceAsStream(fileName);
			String[] lineVals = value.split("//r//n");
					
			sb.append("(");
			for(int i = 0; i < lineVals.length; i++) {
				if (lineVals[i]!=null&&!"".equals(lineVals[i].toString())) {
					sb.append("'").append(lineVals[i].trim()).append("',");
				}
			}
			sb.append(")");
		} finally {
			
		}  
		return sb.toString();
	}
	

}
