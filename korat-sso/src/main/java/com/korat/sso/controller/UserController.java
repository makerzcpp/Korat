package com.korat.sso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.httpclient.HttpResult;
import com.korat.sso.domain.User;
import com.korat.sso.service.CartCookieService;
import com.korat.sso.service.LoginService;
import com.korat.sso.service.UserService;
import com.korat.sso.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户跳转
 *
 * @author solar
 * @date 2017/6/24
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CartCookieService cartCookieService;
    @Value("${COOKIE_NAME}")
    private String COOKIE_NAME;
    @Value("${COOKIE_ITEM_NAME}")
    private String COOKIE_ITEM_NAME;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 跳转到登录
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 跳转方法
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "register")
    public String toRegister() {
        return "register";
    }


    /**
     * 验证用户是否存在
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/{param}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("param") String param, @PathVariable("type") Integer type) {
        try {
            Boolean result = userService.check(param, type);
            if (result != null) {
                //和前端逻辑不一样，修改此处
                return ResponseEntity.ok(false);
            }
            //参数不合法
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 使用hibernate验证框架
     *
     * @param user
     * @param bindingResult
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "doRegister")
    public ResponseEntity<Map<String, Object>> saveUser(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (bindingResult.hasErrors()) {
                List<String> msgs = new ArrayList<>();
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                for (ObjectError objectError : allErrors) {
                    msgs.add(objectError.getDefaultMessage());
                }
                result.put("status", "400");
                result.put("data", StringUtils.join(msgs, '|'));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            Boolean flag = userService.saveUser(user);
            if (flag) {
                result.put("status", "200");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestParam("username") String userName
            , @RequestParam("password") String password,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            String token = loginService.doLogin(userName, password);

            String value = CookieUtils.getCookieValue(request, COOKIE_ITEM_NAME, true);
            if (StringUtils.isNotEmpty(value)) {
                //持久化cookie中的数据
                cartCookieService.persistenceCart(request, COOKIE_ITEM_NAME, userName);
            }
            if (token == null) {
                result.put("status", "400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            } else {
                result.put("status", "200");
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 从redis中找到用户信息
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/query/{token}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> queryUserName(@PathVariable("token") String token) {
        Map<String, Object> result = new HashMap<>();
        User user = new User();
        user.setUsername("该服务已经被弃用，转为dubbo或在ssoquery.korat.com");
        String data = null;
        try {
            data = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.put("status", "404");
        result.put("data", data);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
