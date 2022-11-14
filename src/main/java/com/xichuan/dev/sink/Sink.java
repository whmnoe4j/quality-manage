package com.xichuan.dev.sink;

/**
 * @Author Xichuan
 * @Date 2022/4/13 14:14
 * @Description
 */

/**
 * 结果输出
 */
public interface Sink<T> {

    /**
     * 将结果输出
     * @param t
     * @return
     */
    boolean write(T t);
}
