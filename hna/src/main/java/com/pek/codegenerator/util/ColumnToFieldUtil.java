package com.pek.codegenerator.util;

public class ColumnToFieldUtil {

	public static String  columnToFieldUtil(String columnName){
		if (columnName!=null) {
			columnName = columnName.trim().toLowerCase();

			if (columnName.indexOf("_") < 0) {
				return columnName;
			} else {
				char[] fName = columnName.toCharArray();
				StringBuffer sb = new StringBuffer();
				boolean flagNextToUpper = false;
				for (char c : fName) {
					if ('_' == c) {
						flagNextToUpper = true;
					} else {
						if(flagNextToUpper) {
							sb.append(String.valueOf(c).toUpperCase());
						} else {
							sb.append(c);
						}
						flagNextToUpper = false;
					}
				}
				return sb.toString();
			}
		}
		return columnName;
	}
	
	/**
     * abc_def ->AbcDef
     * 
     * @param str
     * @return
     */
    public static String toUpperCase(String str) {
        String propertie = toLowerCase(str);
        return propertie.substring(0, 1).toUpperCase() + propertie.substring(1);
    }

    /**
     * abc_def ->abcDef
     * 
     * @param str
     * @return
     */
    public static String toLowerCase(String columnName) {
        String attribute = columnName.toLowerCase().trim();
        int index = -1;
        while ((index = attribute.indexOf("_")) != -1) {
            String str = attribute.substring(index + 1).trim();
            if ("".equals(str.trim())) {
                attribute = attribute.substring(0, index).trim();
                break;
            }
            attribute = attribute.substring(0, index).trim() + str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return attribute;
    }
    
//    public static String  columnToFieldUtil(String columnName){
//		if (columnName!=null) {
//			columnName = columnName.trim().toLowerCase();
//
//			if (columnName.indexOf("_") < 0) {
//				return columnName;
//			} else {
//				char[] fName = columnName.toCharArray();
//				StringBuffer sb = new StringBuffer();
//				boolean flagNextToUpper = false;
//				for (char c : fName) {
//					if ('_' == c) {
//						flagNextToUpper = true;
//					} else {
//						if(flagNextToUpper) {
//							sb.append(String.valueOf(c).toUpperCase());
//						} else {
//							sb.append(c);
//						}
//						flagNextToUpper = false;
//					}
//				}
//				return sb.toString();
//			}
//		}
//		return columnName;
//	}
	
}
