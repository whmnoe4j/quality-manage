package com.xichuan.dev.entity;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:35
 * @Description
 */

/**
 * JDBC连接信息
 */
public class JDBCEntity {
    private String driver;
    private String url;
    private String database;
    private String user;
    private String password;

    public JDBCEntity(){}

    public JDBCEntity(String driver, String url, String database, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
