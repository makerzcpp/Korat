package com.korat.sso.controller;

import com.korat.sso.service.LoginService;
import com.korat.sso.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @author solar
 * @date 2017/6/24
 */
@Controller
@RequestMapping("user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    private final String COOKIE_NAME = "KORAT_TOKEN";
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public Map<String, Object> doLogin(@RequestParam("username") String userName
                                        , @RequestParam("password") String password,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            String token = loginService.doLogin(userName, password);
            if (token == null) {
                result.put("status", "400");
            }else {
                result.put("status", "200");
                CookieUtils.setCookie(request,response,COOKIE_NAME,token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
        }
        return result;
    }
}
