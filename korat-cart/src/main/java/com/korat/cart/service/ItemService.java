package com.korat.cart.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.korat.cart.bean.Item;
import com.korat.common.service.ApiService;
import com.korat.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 前台service
 *
 * @author solar
 * @date 2017/6/20
 */
@Service(value = "CART_ITEMSERVICE")
public class ItemService {
    @Autowired
    private ApiService apiService;

    @Value("${KORAT_MANAGE_ITEM_URL}")
    private String KORAT_MANAGE_ITEM_URL;
    @Autowired
    private RedisService redisService;
    @Value("${REDIS_KEY}")
    private String REDIS_KEY;
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 根据商品id查商品
     * @param itemId
     * @return
     */
    public Item queryItemByItemId(Long itemId) {
        String url = KORAT_MANAGE_ITEM_URL + "/api/item/" + itemId;
        try {
            String data = apiService.doGet(url);
            Item item=objectMapper.readValue(data, Item.class);
            redisService.set(REDIS_KEY + item.getId(), data, 60 * 30);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
