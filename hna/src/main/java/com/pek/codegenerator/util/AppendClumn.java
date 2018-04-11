package com.pek.codegenerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;


public class AppendClumn {
	//换行回车
	private static final String COMMAND_RN = "\r\n";
	private static final String daoSourceOrmFile = "sourceorm.xml";
	private static final String daoTargetOrmFile = "targetorm.xml";
	
	private static final String nodeNameResultMap = "resultMap";
	private static final String nodeResultMapId = "";
	private static final String nodeNameSql = "sql";
	private static final String nodeSqlId = "";
	private static final String nodeNameSelect = "select";
	private static final String nodeSqlSelectId = "";
	private static final String nodeNameInsert = "insert";
	private static final String nodeSqlinsertId= "";
	private static final String nodeNameUpdate = "update";
	private static final String nodeSqlUpdateId = "";
	
	public static void main(String[] args) {
		createAppendContent("");
	}
	
//	public static void main(String[] args) {
//		
//		
//		try {
//			String clazzPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//			String fliePath = clazzPath + File.separator + "sourceorm.xml";
//			Document doc = null;
//			SAXReader saxReader = new SAXReader();
//			doc = saxReader.read(new InputStreamReader( new FileInputStream(fliePath), "UTF-8"));
//			Element root = doc.getRootElement();
//			
//			Node node = (Node) root;
//			List<Node> nodeResultMap = node.selectNodes(nodeNameResultMap);
//			
//			List<Node> nodeSql = node.selectNodes(nodeNameSql);
//			
//			List<Node> nodeSelect = node.selectNodes(nodeNameSelect);
//			
//			List<Node> nodeInsert = node.selectNodes(nodeNameInsert);
//			
//			List<Node> nodeUpdate = node.selectNodes(nodeNameUpdate);
//			
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
		
//	}
	
	
	public static void toPrintln(String s){
		System.out.println(s);
	}
	
	public static void createAppendContent(String selectPrefix){
		String content = FileReadUtil.readFile("field.txt");
		String[] arry = content.replaceAll("\\s", "").split(",");
		if (arry == null) {
			toPrintln("NULL");
		}
		Map<String, String> dualMap = new HashMap<String, String>();
		for (int i = 0; i < arry.length; i++) {
			String fieldName = ColumnToFieldUtil.columnToFieldUtil(arry[i]);
			toPrintln(fieldName);
			dualMap.put(arry[i], fieldName);
		}
		
		StringBuffer sb = new StringBuffer(30);
		sb.append("*********MAP***********").append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append("<result column=\"").append(key).append("\"");
			sb.append(" property=\"").append(dualMap.get(key)).append("\"/>").append(COMMAND_RN);
		}
		sb.append(COMMAND_RN);
		
		sb.append("*********SELECT begin***********").append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append(", ");
			if (selectPrefix != null && selectPrefix != "") {
				sb.append(selectPrefix).append(".");
			}
			sb.append(key);
		}
		sb.append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append("<isNotEmpty prepend=\"AND\" property=\"").append(dualMap.get(key)).append("\">").append(COMMAND_RN);
			sb.append("    ").append(key).append("=").append("#").append(dualMap.get(key)).append("#").append(COMMAND_RN);
			sb.append("</isNotEmpty>").append(COMMAND_RN);
		}
		sb.append(COMMAND_RN);
		
		sb.append("*********SELECT end***********").append(COMMAND_RN);
		sb.append(COMMAND_RN);
		
		sb.append("*********INSERT begin***********").append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append("<isNotEmpty prepend=\",\" property=\"").append(dualMap.get(key)).append("\">").append(COMMAND_RN);
			sb.append("    ").append(key).append(COMMAND_RN);
			sb.append("</isNotEmpty>").append(COMMAND_RN);
		}
		sb.append("********************").append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append("<isNotEmpty prepend=\",\" property=\"").append(dualMap.get(key)).append("\">").append(COMMAND_RN);
			sb.append("    #").append(dualMap.get(key)).append("#").append(COMMAND_RN);
			sb.append("</isNotEmpty>").append(COMMAND_RN);
		}
		sb.append("*********INSERT end***********").append(COMMAND_RN);
		sb.append(COMMAND_RN);
		
		sb.append("*********UPDATE begin***********").append(COMMAND_RN);
		for (String key : dualMap.keySet()) {
			sb.append(" <isNotEmpty prepend=\",\" property=\"").append(dualMap.get(key)).append("\">").append(COMMAND_RN);
			sb.append("    ").append(key).append("=").append("#").append(dualMap.get(key)).append("#").append(COMMAND_RN);
			sb.append("</isNotEmpty>").append(COMMAND_RN);
		}
		sb.append("*********UPDATE end***********").append(COMMAND_RN);
		
		toPrintln(sb.toString());
	}
	

	
}
