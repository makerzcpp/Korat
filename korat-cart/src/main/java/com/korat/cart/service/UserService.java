package com.korat.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.cart.bean.User;
import com.korat.common.service.ApiService;
import com.korat.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * userservice
 *
 * @author solar
 * @date 2017/6/27
 */
@Service(value = "CART_USERSERVICE")
public class UserService {
    @Value("${KORAT_SSO_URL}")
    public String KORAT_SSO_URL;
    @Autowired
    private ApiService apiService;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RedisService redisService;

    /**
     * 根据token查询user
     * @param token
     * @return
     */
    public User queryUserByToken(String token) {
        String data = redisService.get("TOKEN_" + token);
        try {
            if (data != null) {
                User user = objectMapper.readValue(data, User.class);
                if (user != null) {
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //String url = KORAT_SSO_URL + "/service/user/query/"+token;
        //try {
        //    String data = apiService.doGet(url);
        //    if (StringUtils.isEmpty(data)) {
        //        return null;
        //    }
        //    if (StringUtils.contains(data, "code")) {
        //        JsonNode jsonNode = objectMapper.readTree(data);
        //        String value= String.valueOf(jsonNode.get("data"));
        //        StringUtils.replace(value, "\\", "");
        //        User user = objectMapper.readValue(value, User.class);
        //        if (user != null) {
        //            return user;
        //        }
        //    }else {
        //        User user = objectMapper.readValue(data, User.class);
        //        if (user != null) {
        //            return user;
        //        }
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //return null;
    }


    public void doLogin() {
        String url = KORAT_SSO_URL + "/user/login.html";
        try {
            apiService.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
