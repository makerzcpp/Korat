package com.korat.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.RedisService;
import com.korat.sso.domain.User;
import com.korat.sso.mapper.LoginMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author solar
 * @date 2017/6/24
 */
@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private RedisService redisService;
    private ObjectMapper objectMapper=new ObjectMapper();
    /**
     * 用户注册
     * @param userName
     * @param password
     * @return
     */
    public String doLogin(String userName, String password) throws Exception {
        User user=new User();
        user.setId(null);
        user.setUsername(userName);
        user= loginMapper.selectOne(user);
        String pwd=user.getPassword();
        if (!Objects.equals(pwd, DigestUtils.md5Hex(password))) {
            return null;
        }
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + userName);

    //    将token保存到redis中
        redisService.set("Token_" + token, objectMapper.writeValueAsString(user), 60 * 30);

        return token;
    }
}
