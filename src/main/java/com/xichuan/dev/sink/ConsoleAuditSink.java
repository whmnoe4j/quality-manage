package com.xichuan.dev.sink;

import com.xichuan.dev.entity.AuditResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Xichuan
 * @Date 2022/4/13 17:19
 * @Description
 */

/**
 * Console sink
 */
public class ConsoleAuditSink implements Sink<AuditResult> {
    private static Logger logger = LoggerFactory.getLogger(ConsoleAuditSink.class);

    @Override
    public boolean write(AuditResult auditResult) {
        if (auditResult == null){
            logger.warn("AuditResult is null!");
            return false;
        }
        System.out.println(auditResult.toString());
        return true;
    }
}
