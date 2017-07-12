package com.korat.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.ApiService;
import com.korat.web.bean.User;
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
@Service
public class UserService {
    @Value("${KORAT_SSO_URL}")
    public String KORAT_SSO_URL;
    @Autowired
    private ApiService apiService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public User queryUserByToken(String token) {
        String url = KORAT_SSO_URL + "/user/"+token;
        try {
            String data = apiService.doGet(url);
            if (StringUtils.isEmpty(data)) {
                return null;
            }
            JsonNode jsonNode = objectMapper.readTree(data);
            String value=jsonNode.get("data").asText();
            User user = objectMapper.readValue(value, User.class);
            if (user != null) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
