package com.korat.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.ApiService;
import com.korat.common.service.RedisService;
import com.korat.web.bean.User;
import com.korat.web.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Value("${COOKIE_NAME}")
    public String COOKIE_NAME;
    @Autowired
    private ApiService apiService;
    @Autowired
    private RedisService redisService;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
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

    /**
     * 退出登录
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME, "UTF-8");
        //CookieUtils.deleteCookie(request,response,COOKIE_NAME);
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(0);
        redisService.del("TOKEN_"+token);
    }
}
