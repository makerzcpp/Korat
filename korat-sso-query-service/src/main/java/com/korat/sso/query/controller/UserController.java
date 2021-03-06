package com.korat.sso.query.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.sso.query.api.domain.User;
import com.korat.sso.query.api.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 查找用户的controller
 *
 * @author solar
 * @date 2017/7/12
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserQueryService userQueryService;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> queryUserByToken(@PathVariable("token") String token) {
        Map<String, Object> result = new HashMap<>();
        User user = userQueryService.queryUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            String data = objectMapper.writeValueAsString(user);
            result.put("status", "200");
            result.put("data", data);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
