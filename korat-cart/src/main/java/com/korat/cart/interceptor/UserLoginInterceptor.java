package com.korat.cart.interceptor;

import com.korat.cart.bean.User;
import com.korat.cart.service.UserService;
import com.korat.cart.util.CookieUtils;
import com.korat.cart.util.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 *
 * @author solar
 * @date 2017/7/4
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    private static final String COOKIE_NAME="TOKEN";
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        UserThreadLocal.set(null);
        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            return true;
        }
        User user = userService.queryUserByToken(token);
        if (user == null) {
            return true;
        }
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
