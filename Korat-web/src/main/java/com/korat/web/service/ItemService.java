package com.korat.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.korat.manage.domain.ItemDesc;
import com.korat.web.bean.Item;
import com.sun.org.apache.regexp.internal.RE;
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
@Service
public class ItemService {
    @Autowired
    private ApiService apiService;

    @Value("${KORAT_MANAGE_ITEM_URL}")
    private String KORAT_MANAGE_ITEM_URL;

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
            return objectMapper.readValue(data, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    public ItemDesc queryItemParamByItemId(Long itemId) {
        String url=KORAT_MANAGE_ITEM_URL+"/api/item/param/"+itemId;
        try {
            String data=apiService.doGet(url);
            JsonNode jsonNode = objectMapper.readTree(data);
            String paramData =jsonNode.get("paramData").asText();
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(paramData);
            StringBuilder sb = new StringBuilder();
            // solartodo 拼html表单
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
