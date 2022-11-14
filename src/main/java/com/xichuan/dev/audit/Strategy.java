package com.xichuan.dev.audit;

import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.SourceFacade;

/**
 * @Author Xichuan
 * @Date 2022/4/13 9:53
 * @Description
 */

/**
 * 稽核策略接口
 */
public interface Strategy {
    /**
     * 稽核执行方法
     * @param auditRule
     * @Param sourceFacade 输入类
     * @param sinkFacade 输出类
     */
    void deal(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade);
}
