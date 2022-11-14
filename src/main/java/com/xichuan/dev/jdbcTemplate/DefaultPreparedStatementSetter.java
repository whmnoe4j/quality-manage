package com.xichuan.dev.jdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:59
 * @Description
 */
public class DefaultPreparedStatementSetter implements PreparedStatementSetter {
    private Object[] args;
    private int[] argTypes;

    public DefaultPreparedStatementSetter(Object[] args, int[] argTypes) {
        this.args = args;
        this.argTypes = argTypes;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        if(argTypes != null && argTypes.length > 0 && args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i],argTypes[i]);
            }
        }else if(args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
        }

    }
}
