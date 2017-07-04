package com.korat.web.rabbitmq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * 商品处理类
 *
 * @author solar
 * @date 2017/7/2
 */
public class ItemMQHandler {
    @Autowired
    private RedisService redisService;
    @Value("${REDIS_KEY}")
    private String REDIS_KEY;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public void execute(String msg) {
        try {
            JsonNode jsonNode = objectMapper.readTree(msg);
            Long itemId=jsonNode.get("itemId").asLong();
            redisService.del(REDIS_KEY+itemId.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
