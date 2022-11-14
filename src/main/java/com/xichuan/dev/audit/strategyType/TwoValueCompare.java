package com.xichuan.dev.audit.strategyType;

import com.xichuan.dev.audit.AbstractStrategy;
import com.xichuan.dev.audit.Strategy;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.config.annotation.StrategyAnnotation;
import com.xichuan.dev.entity.AuditResult;
import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.SourceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Xichuan
 * @Date 2022/4/13 15:57
 * @Description
 */
@RegisterBean
@StrategyAnnotation(name = "two_value_compare",description = "两表中两个字段数值比较")
public class TwoValueCompare extends AbstractStrategy implements Strategy {
    private static Logger logger = LoggerFactory.getLogger(TwoValueCompare.class);

    @Override
    protected Object parseRuleJson(String ruleJson) {
        return null;
    }

    @Override
    public AuditResult resolve(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade) {


        AuditResult auditResult = new AuditResult();
        return auditResult;
    }
}