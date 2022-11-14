package com.xichuan.dev.source;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/4/14 11:15
 * @Description
 */

/**
 * AuditSource外观类
 */
public class AuditSourceFacade implements SourceFacade{
    private Map<String,Source> sources = new HashMap<>();
    private static AuditSourceFacade instance = null;

    private AuditSourceFacade(){}

    public static AuditSourceFacade getInstance(){
        if (instance == null){
            instance = new AuditSourceFacade();
        }
        return instance;
    }

    @Override
    public SourceFacade register(String sourceName, Source s) {
        if (sourceName!= null && s != null && !"".equals(sourceName)){
            sources.put(sourceName,s);
        }
        return this;
    }

    @Override
    public Source get(String sourceName) {
        if (sourceName!= null && !"".equals(sourceName)){
            return sources.get(sourceName);
        }
        return null;
    }
}
