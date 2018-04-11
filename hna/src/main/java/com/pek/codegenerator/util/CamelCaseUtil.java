package com.pek.codegenerator.util;


public class CamelCaseUtil {
    /**
     * abc_def ->AbcDef
     * 
     * @param columnName
     * @return
     */
    public static String getFirstUpperCamelCaseName(String columnName) {
        String name = getFirstLowerCamelCaseName(columnName);
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * abc_def ->abcDef
     * 
     * @param str
     * @return
     */
    public static String getFirstLowerCamelCaseName(String columnName) {
        String name = columnName.toLowerCase().trim();
        int index = -1;
        while ((index = name.indexOf("_")) != -1) {
            String str = name.substring(index + 1).trim();
            if ("".equals(str.trim())) {
                name = name.substring(0, index).trim();
                break;
            }
            name = name.substring(0, index).trim() + str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return name;
    }
}
