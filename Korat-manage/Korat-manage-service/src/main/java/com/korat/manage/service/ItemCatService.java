package com.korat.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.korat.manage.domain.Item;
import com.korat.manage.domain.ItemCat;
import com.korat.manage.domain.ItemCatData;
import com.korat.manage.domain.ItemCatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品业务层
 *
 * @author solar
 * @date 2017/6/12
 */
@Service
public class ItemCatService extends BaseService<ItemCat> {


    @Autowired
    private Mapper<ItemCat> mapper;

    public ItemCatResult queryAllToTree() {
      //  solartodo 封装1~3级目录
      return null;
    }
}
