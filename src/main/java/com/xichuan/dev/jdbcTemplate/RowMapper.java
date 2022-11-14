package com.xichuan.dev.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:55
 * @Description
 */
public interface RowMapper<T> {
    <T>T mapRow(ResultSet rs, int rowNum) throws SQLException;
}

