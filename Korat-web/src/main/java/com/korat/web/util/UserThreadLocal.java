package com.korat.web.util;

import com.korat.web.bean.User;

/**
 * 将登陆的user放入到ThreadLocal中
 *
 * @author solar
 * @date 2017/7/4
 */
public class UserThreadLocal {
    private static final ThreadLocal<User> threadLocal=new ThreadLocal<>();

    public static User get() {
        return threadLocal.get();
    }

    public static void set(User user) {
        threadLocal.set(user);
    }

}
