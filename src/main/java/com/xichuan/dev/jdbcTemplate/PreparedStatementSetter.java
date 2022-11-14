package com.xichuan.dev.jdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:58
 * @Description
 */
public interface PreparedStatementSetter {
    void setValues(PreparedStatement ps) throws SQLException;
}

