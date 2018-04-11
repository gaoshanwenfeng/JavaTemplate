package com.pek.codegenerator.pojo.pojo;

import java.util.ArrayList;
import java.util.List;

import com.pek.codegenerator.util.CamelCaseUtil;



public class Table {
    private String tableName;
    List<Column> columns = new ArrayList<Column>();

    private String pk;

    public void addColumn(Column column) {
        columns.add(column);
    }

    public String getByIdSql() {
        String sql = "";
        for (Column c : columns) {
            if (c.isPk()) {
                sql += " AND T." + c.getColumnName() + "=#" + c.getFirstLowerCamelCaseName() + "#";
            }
        }
        return "WHERE" + sql.substring(4);
    }

    public String getSelectSql() {
        String sql = "";
        for (Column c : columns) {
            sql += ",T." + c.getColumnName();
        }
        return "SELECT " + sql.substring(1) + " \n\t\t\tFROM " + tableName.toUpperCase() + " T";
    }

    public String getDeleteSql() {
        String sql = "DELETE FROM ";
        sql += tableName.toUpperCase() + " T ";
        return sql + "\n\t\t\t" + getByIdSql();
    }

    public String getUpdateSql() {
        String sql = "";
        for (Column c : columns) {
            if (!c.isPk()) {
                sql += "," + c.getColumnName() + "=";
                if (true) {
                    sql += "#" + c.getFirstLowerCamelCaseName() + "#";
                }
            }
        }
        sql = "UPDATE " + tableName.toUpperCase() + " \n\t\t\tSET " + sql.substring(1);
        sql += "\n\t\t\t" + getByIdSql();
        return sql;
    }

    public String getInsertSql() {
        String sql = "";
        String value = "";
        for (Column c : columns) {
            sql += "," + c.getColumnName();
            value += ",#" + c.getFirstLowerCamelCaseName() + "#";
        }
        sql = "INSERT INTO " + tableName.toUpperCase() + " (" + sql.substring(1) + ") \n\t\t\tVALUES (" + value.substring(1) + ")";
        return sql;
    }

    public String getTableName() {
        return tableName.toLowerCase();
    }

    public String getFirstUpperCamelCaseName() {
        return CamelCaseUtil.getFirstUpperCamelCaseName(this.tableName);
    }

    public String getFirstLowerCamelCaseName() {
        return CamelCaseUtil.getFirstLowerCamelCaseName(this.tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
