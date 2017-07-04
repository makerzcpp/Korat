package com.korat.cart.util;

import com.korat.cart.bean.User;

/**
 * 存储user到本地线程
 *
 * @author solar
 * @date 2017/7/4
 */
public class UserThreadLocal{
    private static final ThreadLocal<User> threadLocal=new ThreadLocal<>();

    public static User get() {
        return threadLocal.get();
    }

    public static void set(User user) {
        threadLocal.set(user);
    }
}
