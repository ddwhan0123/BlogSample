package com.wjj.numberdemo.listener;

/**
 * Created by jiajiewang on 16/9/2.
 */
public interface ReadListener<T> {
    //开始流程
    void start();

    //流程进行中
    void doing(T t);

    //流程结束
    void finish(T t);
}
