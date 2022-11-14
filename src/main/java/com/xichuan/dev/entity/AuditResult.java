package com.xichuan.dev.entity;

import java.sql.Timestamp;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:10
 * @Description
 */

/**
 * 稽核结果类
 */
public class AuditResult {

    private Integer id;

    private Integer ruleId;

    private String result;

    private String isPass;

    private Timestamp createTime;

    public AuditResult(){}

    public AuditResult(Integer id, Integer ruleId, String result, String isPass, Timestamp createTime) {
        this.id = id;
        this.ruleId = ruleId;
        this.result = result;
        this.isPass = isPass;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AuditResult{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", result='" + result + '\'' +
                ", isPass='" + isPass + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
