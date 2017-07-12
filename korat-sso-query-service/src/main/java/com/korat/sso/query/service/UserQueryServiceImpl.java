package com.korat.sso.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.RedisService;
import com.korat.sso.query.api.domain.User;
import com.korat.sso.query.api.service.UserQueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 从redis中查询用户
 *
 * @author solar
 * @date 2017/7/12
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    private RedisService redisService;
    private static final String REDIS_KEY = "TOKEN_";
    private static final Integer REDIS_TIME=60*30;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public User queryUserByToken(String token) {
        String url=REDIS_KEY+token;
        String redisData = redisService.get(url);
        if (StringUtils.isEmpty(redisData)) {
            return null;
        }
        this.redisService.expirt(REDIS_KEY, REDIS_TIME);
        try {
            return objectMapper.readValue(redisData, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
