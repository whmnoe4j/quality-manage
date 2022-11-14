package com.xichuan.dev.sink;

import com.xichuan.dev.entity.AuditResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Xichuan
 * @Date 2022/4/13 19:21
 * @Description
 */

/**
 * AuditSink外观类
 */
public class AuditSinkFacade implements SinkFacade<AuditResult>{
    private static List<Sink> sinks = new ArrayList<>();
    private static AuditSinkFacade instance = null;

    private AuditSinkFacade(){}

    public static AuditSinkFacade getInstance(){
        if (instance == null){
            instance = new AuditSinkFacade();
        }
        return instance;
    }

    /**
     * 添加sink
     * @param sink
     * @return
     */
    @Override
    public AuditSinkFacade addSink(Sink sink){
        if (sink != null){
            sinks.add(sink);
        }
        return this;
    }

    /**
     * 执行sink输出
     */
    @Override
    public void execute(AuditResult auditResult){
        for (Sink sink:sinks){
            sink.write(auditResult);
        }
    }
}
