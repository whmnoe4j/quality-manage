package com.xichuan.dev.entity.strategyType;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author Xichuan
 * @Date 2022/4/15 15:52
 * @Description
 */
public class CustomSQLRule {
    @JSONField(name="source_type")
    private String sourceType;

    private String sql;


    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
