package com.pek.codegenerator.pojo.pojo;



import java.util.ArrayList;
import java.util.List;

import com.pek.codegenerator.util.CamelCaseUtil;



public class Table {
    private String tableName;
    List<Column> columns = new ArrayList<Column>();

    private String pk;

    private boolean isUpperCase;

    public void addColumn(Column column) {
        columns.add(column);
    }

    public String getByIdSql() {
    	String sql = "";
    	if (isUpperCase) {
    		for (Column c : columns) {
    			if (c.isPk()) {
    				sql += " AND T." + c.getColumnName() + "=#" + c.toLowerCase() + "#";
    			}
    		}
    		return "WHERE" + sql.substring(4);
    	} else {
    		for (Column c : columns) {
    			if (c.isPk()) {
    				sql += " and t." + c.getColumnName() + "=#" + c.toLowerCase() + "#";
    			}
    		}
    		return "where" + sql.substring(4);
    	}
    }

    public String getSelectSql() {
    	String sql = "";
    	if (isUpperCase) {
	        for (Column c : columns) {
	            sql += ",T." + c.getColumnName();
	        }
	        return "SELECT " + sql.substring(1) + " \n\t\t\tFROM " + tableName.toUpperCase() + " T";
    	} else {
   	        for (Column c : columns) {
   	            sql += ",t." + c.getColumnName();
   	        }
   	        return "select " + sql.substring(1) + " \n\t\t\t" + " from " + tableName.toLowerCase() + " t";
    	}
    }

    public String getDeleteSql() {
    	if (isUpperCase) {
	        String sql = "DELETE FROM ";
	        sql += tableName.toUpperCase() + " T ";
	        return sql + "\n\t\t\t" + getByIdSql();
    	} else {
    		String sql = "delete from ";
	        sql += tableName.toLowerCase() + " t ";
	        return sql + "\n\t\t\t" + getByIdSql();
    	}
    }

    public String getUpdateSql() {
    	String sql = "";
    	if (isUpperCase) {
    		for (Column c : columns) {
    			if (!c.isPk()) {
    				sql += "," + c.getColumnName() + "=";
    				if (true) {
    					sql += "#" + c.toLowerCase() + "#";
    				}
    			}
    		}
    		sql = "UPDATE " + tableName.toUpperCase() + " \n\t\t\tSET " + sql.substring(1);
    		sql += "\n\t\t\t" + getByIdSql();
    		
    	} else {
    		for (Column c : columns) {
    			if (!c.isPk()) {
    				sql += "," + c.getColumnName() + "=";
    				if (true) {
    					sql += "#" + c.toLowerCase() + "#";
    				}
    			}
    		}
    		sql = "update " + tableName.toLowerCase() + " \n\t\t\t set " + sql.substring(1);
    		sql += "\n\t\t\t" + getByIdSql();
    	}
        return sql;
    }

    public String getInsertSql() {
        String sql = "";
        String value = "";
    	if (isUpperCase) {
	        for (Column c : columns) {
	            sql += "," + c.getColumnName();
	            value += ",#" + c.toLowerCase() + "#";
	        }
	        sql = "INSERT INTO " + tableName.toUpperCase() + " (" + sql.substring(1) + ") \n\t\t\tVALUES ("
	            + value.substring(1) + ")";
    	} else {
	        for (Column c : columns) {
	            sql += "," + c.getColumnName();
	            value += ",#" + c.toLowerCase() + "#";
	        }
	        sql = "insert into " + tableName.toUpperCase() + " (" + sql.substring(1) + ") \n\t\t\t values ("
	            + value.substring(1) + ")";	
    	}
        return sql;
    }

    public String getTableName() {
        return tableName.toLowerCase();
    }

    public String toUpperCase() {
        return CamelCaseUtil.getFirstUpperCamelCaseName(this.tableName);
    }

    public String toLowerCase() {
        return CamelCaseUtil.getFirstLowerCamelCaseName(this.tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    //	public String getDomain() {
    //		return domain;
    //	}
    //
    //	public void setDomain(String domain) {
    //		this.domain = domain;
    //	}

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

	public boolean isUpperCase() {
		return isUpperCase;
	}

	public void setUpperCase(boolean isUpperCase) {
		this.isUpperCase = isUpperCase;
	}

}
