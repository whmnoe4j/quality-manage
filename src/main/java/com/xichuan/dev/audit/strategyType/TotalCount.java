package com.xichuan.dev.audit.strategyType;

import com.xichuan.dev.audit.AbstractStrategy;
import com.xichuan.dev.audit.Strategy;
import com.xichuan.dev.config.Constants;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.config.annotation.StrategyAnnotation;
import com.xichuan.dev.entity.AuditResult;
import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.entity.strategyType.BaseRule;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.JDBCSource;
import com.xichuan.dev.source.SourceFacade;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:57
 * @Description
 */
@RegisterBean
@StrategyAnnotation(name = "total_count",description = "total数量校验")
public class TotalCount extends AbstractStrategy<BaseRule> implements Strategy {
    private static Logger logger = LoggerFactory.getLogger(TotalCount.class);

    /**
     * 解析RuleJson
     *
     * @param ruleJson
     * @return
     */
    @Override
    protected BaseRule parseRuleJson(String ruleJson) {
        return JSON.parseObject(ruleJson, BaseRule.class);
    }

    /**
     * 具体处理逻辑
     *
     * @param auditRule
     * @param sourceFacade
     * @param sinkFacade   输出类
     * @return
     */
    @Override
    public AuditResult resolve(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade) {
        AuditResult auditResult = null;
        try {
            //source
            JDBCSource source = (JDBCSource) sourceFacade.get(rule.getSourceType());
            if (source == null) {
                logger.warn("source is null !");
                return auditResult;
            }

            // execute
            String sql = MessageFormat.format("select count({0}) as ct from {1}.{2}", rule.getColumn(), rule.getDatabase(), rule.getTable());
            Integer count = source.read(sql, rs -> {
                Integer ct = 0;
                while (rs.next()) {
                    ct = rs.getInt("ct");
                }
                return ct;
            });

            //sink result
            auditResult = new AuditResult();
            auditResult.setResult(String.valueOf(count));
            auditResult.setIsPass(count <= Integer.valueOf(auditRule.getRangeMin()) ? Constants.RULE_RESULT_NOT_PASS : Constants.RULE_RESULT_PASS);
            auditResult.setRuleId(auditRule.getId());
        } catch (Exception e) {
            logger.error("处理distinct_count异常！," + e.getMessage());
            e.printStackTrace();
        }

        return auditResult;
    }
}
