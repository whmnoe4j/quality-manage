package com.xichuan.dev.source;

/**
 * @Author Xichuan
 * @Date 2022/4/14 11:11
 * @Description
 */
public interface SourceFacade {

    //注册Source
    SourceFacade register(String sourceName,Source s);

    //获取Source
    Source get(String sourceName);

}
