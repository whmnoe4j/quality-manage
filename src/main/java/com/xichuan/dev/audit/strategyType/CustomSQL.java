package com.xichuan.dev.audit.strategyType;

import com.xichuan.dev.audit.AbstractStrategy;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.config.annotation.StrategyAnnotation;
import com.xichuan.dev.entity.AuditResult;
import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.entity.strategyType.CustomSQLRule;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.JDBCSource;
import com.xichuan.dev.source.SourceFacade;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:59
 * @Description
 */
@RegisterBean
@StrategyAnnotation(name = "custom_sql",description = "自定义sql校验")
public class CustomSQL extends AbstractStrategy<CustomSQLRule>{
    private static Logger logger = LoggerFactory.getLogger(CustomSQL.class);


    /**
     * 解析RuleJson
     * @param ruleJson
     * @return
     */
    @Override
    protected CustomSQLRule parseRuleJson(String ruleJson) {
        return JSON.parseObject(ruleJson,CustomSQLRule.class);
    }

    /**
     * 具体处理逻辑
     * @param auditRule
     * @param sourceFacade
     * @param sinkFacade 输出类
     * @return
     */
    @Override
    public AuditResult resolve(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade) {
        AuditResult auditResult = null;
        try {
            //source
            JDBCSource source = (JDBCSource) sourceFacade.get(rule.getSourceType());
            if (source == null){
                logger.warn("source is null !");
                return auditResult;
            }

            // execute
            CustomResult cr = source.read(rule.getSql(), rs -> {
                CustomResult r = new CustomResult();
                while (rs.next()){
                    r.setIsPass(rs.getString("is_pass"));
                    r.setResult(rs.getString("result"));
                }
                return r;
            });

            //sink result
            auditResult = new AuditResult();
            auditResult.setResult(cr.getResult());
            auditResult.setIsPass(cr.getIsPass());
            auditResult.setRuleId(auditRule.getId());
        } catch (Exception e) {
            logger.error("run custom_sql rule exception！,"+e.getMessage());
            e.printStackTrace();
        }

        return auditResult;
    }
}


class CustomResult{
    private String isPass;
    private String result;

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
