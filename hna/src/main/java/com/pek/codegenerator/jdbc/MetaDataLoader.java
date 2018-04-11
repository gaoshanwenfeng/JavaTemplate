package com.pek.codegenerator.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.pek.codegenerator.pojo.pojo.Column;
import com.pek.codegenerator.pojo.pojo.Table;

public class MetaDataLoader {
    public static int getDbType(Connection con) {
        String dbName = null;
        int dbType = DatabaseType.UNKNOWN;
        try {
            dbName = con.getMetaData().getDatabaseProductName();
            dbName = dbName.toLowerCase();
            if (-1 != dbName.indexOf("sql server")) {
                dbType = DatabaseType.MSSQL;
            } else if (-1 != dbName.indexOf("oracle")) {
                dbType = DatabaseType.ORACLE;
            } else if (dbName.indexOf("db2") != -1) {
                dbType = DatabaseType.DB2;
            } else if (dbName.indexOf("hsql") != -1) {
                dbType = DatabaseType.HSQL;
            } else if (dbName.indexOf("mysql") != -1) {
                dbType = DatabaseType.MYSQL;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbType;
    }

    private static String getColumnType(String type) {
        if (type.equalsIgnoreCase("CHAR")) {
            return "String";
        } else if (type.equalsIgnoreCase("NVARCHAR2")) {
            return "String";
        } else if (type.equalsIgnoreCase("VARCHAR2")) {
            return "String";
        } else if (type.equalsIgnoreCase("NUMBER")) {
            return "BigDecimal";
        } else if (type.equalsIgnoreCase("LONG")) {
            return "Long";
        } else if (type.equalsIgnoreCase("DATE")) {
            return "Date";
        } else if (type.equalsIgnoreCase("TIMESTAMP(6)")) {
            return "Date";
        }
        return "String";
    }

    private static String getColumnType(int type) {
        switch (type) {
            case Types.BOOLEAN:
                return "boolean";
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.DATE:
                return "Date";
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.CLOB:
            case Types.BLOB:
                return "String";
            case Types.INTEGER:
            case Types.NUMERIC:
                return "BigDecimal";
            default:
                return "String";
        }
    }

    private static Table loadMySqlMetaData(Connection connection, Table table) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table.getTableName().toUpperCase() + " WHERE 1!=1");
        ps.execute();
        ResultSetMetaData rsmd = ps.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnName(i);
            String columnType = getColumnType(rsmd.getColumnType(i));
            Column column = new Column(columnName, columnType, "");
            if (table.getPk().toUpperCase().equalsIgnoreCase(columnName)) {
                column.setPk(true);
            }
            table.addColumn(column);
        }
        return table;
    }

    private static Table loadOracleMetaData(Connection connection, Table table) throws SQLException {
        PreparedStatement ps = null;
        if (getDbType(connection) == DatabaseType.ORACLE) {
            ps = connection.prepareStatement(DatabaseSql.ORACLE_SQL);
        } else if (getDbType(connection) == DatabaseType.MYSQL) {
            ps = connection.prepareStatement(DatabaseSql.MYSQL_SQL);
        }
        ps.setString(1, table.getTableName().toUpperCase());
        ResultSet rs = ps.executeQuery();
        String columnType;
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("DATA_TYPE");
            String comments = rs.getString("COMMENTS");

            //ORACLE没指定精度的number类型字段，java对应为Long
            if (dataType.equalsIgnoreCase("NUMBER")) {
                String PRECISION = rs.getString("DATA_PRECISION");
                String DATA_SCALE = rs.getString("DATA_SCALE");
                if (PRECISION == null && DATA_SCALE == null) {
                    dataType = "LONG";
                }
            }

            columnType = getColumnType(dataType);
            Column column = new Column(columnName, columnType, comments);
            if (table.getPk().equalsIgnoreCase(columnName)) {
                column.setPk(true);
            }
            if ("String".equals(columnType)) {
                column.setString(true);
            }
            table.addColumn(column);
        }
        return table;
    }

    public static Table loadMetaData(String tableName, String pkFieldName) {
        Connection connection = ConnectionFactory.getConnection();
        Table table = new Table();
        table.setTableName(tableName);
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, table.getTableName());
            String pk = pkFieldName;
            if (pkRSet.next()) {
                pk = (String)pkRSet.getObject(4);
            }
            table.setPk(pk);
            int dbType = getDbType(connection);
            if (dbType == DatabaseType.MYSQL) {
                table = loadMySqlMetaData(connection, table);
            } else if (dbType == DatabaseType.ORACLE) {
                table = loadOracleMetaData(connection, table);
            } else {
                table = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return table;
    }

}
