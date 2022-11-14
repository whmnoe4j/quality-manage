package com.xichuan.dev.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:57
 * @Description
 */
public interface RowCallbackHandler {
    void processRow(ResultSet rs) throws SQLException;
}
