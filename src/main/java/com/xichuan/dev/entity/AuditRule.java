package com.xichuan.dev.entity;

import java.sql.Timestamp;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:12
 * @Description
 */

/**
 * 稽核规则类
 */
public class AuditRule {

    private Integer id;

    private String name;

    private String auditType;

    private String strategy;

    private String ruleJson;

    private String singleValue;

    private String rangeMin;

    private String rangeMax;

    private Integer notificationLevel;

    private String description;

    private String status;

    private Timestamp creatTime;

    public AuditRule(){}

    public AuditRule(Integer id, String name, String auditType, String strategy, String ruleJson, String singleValue, String rangeMin, String rangeMax, Integer notificationLevel, String description, String status, Timestamp creatTime) {
        this.id = id;
        this.name = name;
        this.auditType = auditType;
        this.strategy = strategy;
        this.ruleJson = ruleJson;
        this.singleValue = singleValue;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.notificationLevel = notificationLevel;
        this.description = description;
        this.status = status;
        this.creatTime = creatTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getSingleValue() {
        return singleValue;
    }

    public void setSingleValue(String singleValue) {
        this.singleValue = singleValue;
    }

    public String getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(String rangeMin) {
        this.rangeMin = rangeMin;
    }

    public String getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(String rangeMax) {
        this.rangeMax = rangeMax;
    }

    public Integer getNotificationLevel() {
        return notificationLevel;
    }

    public void setNotificationLevel(Integer notificationLevel) {
        this.notificationLevel = notificationLevel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getRuleJson() {
        return ruleJson;
    }

    public void setRuleJson(String ruleJson) {
        this.ruleJson = ruleJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Timestamp creatTime) {
        this.creatTime = creatTime;
    }
}
