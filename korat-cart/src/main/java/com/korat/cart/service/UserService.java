package com.korat.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.cart.bean.User;
import com.korat.common.service.ApiService;
import org.apache.commons.lang3.StringUtils;
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

    public User queryUserByToken(String token) {
        String url = KORAT_SSO_URL + "/service/user/query/TOKEN_"+token;
        try {
            String data = apiService.doGet(url);
            if (StringUtils.isEmpty(data)) {
                return null;
            }
            User user = objectMapper.readValue(data, User.class);
            if (user != null) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
