package com.korat.common.utils;

/**
 * 抽取redis服务
 *
 * @author solar
 * @date 2017/6/20
 */
public interface Function<T,E> {

    T callBack(E e);

}
