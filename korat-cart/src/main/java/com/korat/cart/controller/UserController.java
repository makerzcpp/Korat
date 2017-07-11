package com.korat.cart.controller;

import com.korat.cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用戶登录controller
 *
 * @author solar
 * @date 2017/7/10
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 跳转到登录界面
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "redirect:http://sso.korat.com/user/login.html";
    }
}
