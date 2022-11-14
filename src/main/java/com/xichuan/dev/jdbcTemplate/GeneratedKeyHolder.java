package com.xichuan.dev.jdbcTemplate;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:01
 * @Description
 */
/**
 * 用于接收回传的ID
 */
public class GeneratedKeyHolder implements keyHolder {
    private Integer key;

    @Override
    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public Integer getKey() {
        return key;
    }
}