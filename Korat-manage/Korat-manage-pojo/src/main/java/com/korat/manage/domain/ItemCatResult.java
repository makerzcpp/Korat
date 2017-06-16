package com.korat.manage.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 请求侧栏商品列表
 *
 * @author solar
 * @date 2017/6/16
 */
public class ItemCatResult {

    @JsonProperty("data")
    private List<ItemCatData> itemCats;

    public List<ItemCatData> getItemCats() {
        return itemCats;
    }

    public void setItemCats(List<ItemCatData> itemCats) {
        this.itemCats = itemCats;
    }
}
