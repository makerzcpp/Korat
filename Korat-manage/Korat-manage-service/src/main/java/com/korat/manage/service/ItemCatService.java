package com.korat.manage.service;

import com.korat.manage.domain.ItemCat;
import com.korat.manage.mapper.ItemCatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品业务层
 *
 * @author solar
 * @date 2017/6/12
 */
@Service
public class ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;


    public List<ItemCat> queryItemCart(Long parentId) {
        ItemCat record = new ItemCat();
        record.setParentId(parentId);
        return itemCatMapper.select(record);
    }
}
