package com.xichuan.dev.audit;


/**
 * @Author Xichuan
 * @Date 2022/4/13 9:54
 * @Description
 */

import com.xichuan.dev.entity.AuditResult;
import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.SourceFacade;

/**
 * 抽象策略类
 */
public abstract class AbstractStrategy<T> implements Strategy {

    //解析后的json对象
    protected T rule;

    /**
     * 在StrategyContext中注册策略
     * @param name
     */
    public void register(String name) {
        StrategyContext.registerStrategy(name,this);
    }

    /**
     * 解析rule json
     * @param ruleJson
     * @return
     */
    abstract protected T parseRuleJson(String ruleJson);

    /**
     * 具体的处理逻辑
     * @param auditRule
     * @Param sourceFacade 输入类
     * @param sinkFacade 输出类
     */
    abstract protected AuditResult resolve(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade);

    /**
     * 稽核执行方法
     * @param auditRule
     * @Param sourceFacade 输入类
     * @param sinkFacade 输出类
     */
    @Override
    public void deal(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade) {
        rule = parseRuleJson(auditRule.getRuleJson());
        sinkFacade.execute(resolve(auditRule, sourceFacade, sinkFacade));
    }

}
