package com.xichuan.dev.sink;

/**
 * @Author Xichuan
 * @Date 2022/4/14 10:44
 * @Description
 */
public interface SinkFacade<T> {

     SinkFacade addSink(Sink sink);

     void execute(T t);
}
