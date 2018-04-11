package com.pek.codegenerator.pojo.pojo;

import com.pek.codegenerator.util.CamelCaseUtil;

public class Column {
    private boolean pk;
    private String columnName;
    private String dataType;
    private String comments;
    private boolean isString;

    public Column(String columnName, String dataType, String comments) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.comments = comments;
    }

    public Column() {

    }

    public String getColumnName() {
        return columnName.toUpperCase();
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFirstLowerCamelCaseName() {
        return CamelCaseUtil.getFirstLowerCamelCaseName(this.columnName);
    }

    public String getFirstUpperCamelCaseName() {
        return CamelCaseUtil.getFirstUpperCamelCaseName(this.columnName);
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments == null ? getFirstLowerCamelCaseName() : comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean isString) {
        this.isString = isString;
    }
}
