package com.xichuan.dev.entity.strategyType;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author Xichuan
 * @Date 2022/4/15 13:25
 * @Description
 */
public class BaseRule {
    @JSONField(name="source_type")
    private String sourceType;

    private String database;

    private String table;

    private String column;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
