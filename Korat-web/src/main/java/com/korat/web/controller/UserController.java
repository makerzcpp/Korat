package com.korat.web.controller;

import com.korat.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台中的userController
 *
 * @author solar
 * @date 2017/7/16
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 首页登录
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "redirect:http://sso.korat.com/user/login.html";
    }


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String toLogout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request,response);
        return "redirect:http://www.korat.com";
    }
}
