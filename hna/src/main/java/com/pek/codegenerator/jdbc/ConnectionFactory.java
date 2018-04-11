package com.pek.codegenerator.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionFactory {
    private static ConnectionFactory factory = null;

    private String driver;

    private String url;

    private String user;

    private String password;

    private String jndiName;

    private ConnectionFactory() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        try {
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        driver = pro.getProperty("driver");
        url = pro.getProperty("url");
        user = pro.getProperty("user");
        password = pro.getProperty("password");
        jndiName = pro.getProperty("jndiName");
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static Connection getConnection() {
        Connection con = null;
        if (factory == null) {
            try {
                factory = new ConnectionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName(factory.getDriver());
            con = DriverManager.getConnection(factory.getUrl(), factory.getUser(), factory.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getJNDIConection() {
        Connection con = null;
        if (factory == null) {
            try {
                factory = new ConnectionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource)envCtx.lookup(factory.getJndiName());
            con = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
