package com.xichuan.dev.source;

/**
 * @Author Xichuan
 * @Date 2022/4/13 18:42
 * @Description
 */
public interface Source<T,V> {
    T read(V v) throws Exception;
}
