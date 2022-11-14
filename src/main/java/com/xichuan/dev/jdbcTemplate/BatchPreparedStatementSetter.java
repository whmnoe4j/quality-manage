package com.xichuan.dev.jdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:01
 * @Description
 */
public interface BatchPreparedStatementSetter {
    void setValues(PreparedStatement ps, int i) throws SQLException;

    int getBatchSize();
}
