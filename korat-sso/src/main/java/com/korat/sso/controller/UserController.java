package com.korat.sso.controller;

import com.korat.sso.domain.User;
import com.korat.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    /**
     * 跳转方法
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "register")
    public String toRegister() {
        return "register";
    }


    /**
     * 验证用户是否存在
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/{param}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("param") String param, @PathVariable("type") Integer type) {
        try {
            Boolean result = userService.check(param, type);
            if (result!=null) {
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
     * 保存用户
     * @param user
     * @return
     */
    //@RequestMapping(method = RequestMethod.POST, value = "doRegister")
    //public Map<String, Object> saveUser(User user) {
    //    Map<String, Object> result = new HashMap<>();
    //    try {
    //
    //        Boolean flag = userService.saveUser(user);
    //        if (flag) {
    //            result.put("status", "200");
    //        }else {
    //            result.put("status", "500");
    //            result.put("data", "嘿嘿嘿~");
    //        }
    //        return result;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        result.put("status", "500");
    //        result.put("data", "啪啪啪~");
    //    }
    //    return result;
    //}


    /**
     * 使用hibernate验证框架
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
                List<ObjectError> allErrors=bindingResult.getAllErrors();
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

}
