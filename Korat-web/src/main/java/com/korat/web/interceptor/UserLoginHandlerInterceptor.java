package com.korat.web.interceptor;

import com.korat.web.bean.User;
import com.korat.web.service.UserService;
import com.korat.web.util.CookieUtils;
import com.korat.web.util.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author solar
 * @date 2017/6/27
 */
public class UserLoginHandlerInterceptor implements HandlerInterceptor{
    @Autowired
    private UserService userService;
    @Value("${COOKIE_NAME}")
    private String COOKIE_NAME;
    @Value("${KORAT_SSO_URL}")
    private String KORAT_SSO_URL;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        UserThreadLocal.set(null);
        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            httpServletResponse.sendRedirect(KORAT_SSO_URL +"/user/login.html");
            return false;
        }
        User user = userService.queryUserByToken(token);
        if (user == null) {
            httpServletResponse.sendRedirect(KORAT_SSO_URL +"/user/login.html");
            return false;
        }
        UserThreadLocal.set(user);
        //登录成功
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
