package com.xichuan.dev.jdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:00
 * @Description
 */
public class DefaultPreparedStatementCreator implements PreparedStatementCreator {
    private PreparedStatementSetter setter;
    private String sql;

    public DefaultPreparedStatementCreator(PreparedStatementSetter setter,String sql) {
        this.setter = setter;
        this.sql = sql;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        setter.setValues(ps);
        return ps;
    }
}
