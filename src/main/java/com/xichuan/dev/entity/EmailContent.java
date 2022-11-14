package com.xichuan.dev.entity;

/**
 * @Author Xichuan
 * @Date 2022/4/18 16:08
 * @Description
 */
public class EmailContent {
    private String ruleId;
    private String name;
    private String auditType;
    private String result;
    private String createTime;
    private String isPass;
    private String strategy;
    private String ruleJson;
    private String description;

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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
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
}
