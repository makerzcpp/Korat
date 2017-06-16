package com.korat.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.korat.manage.domain.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品规格最终数据业务层
 *
 * @author solar
 * @date 2017/6/15
 */
@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {
    @Autowired
    private Mapper<ItemParamItem> mapper;

    /**
     * 更新商品规格信息
     * @param itemParamItem
     * @return
     */
    public Integer updateItemParamsByExample(ItemParamItem itemParamItem) {
        Example example = new Example(ItemParamItem.class);
        example.or().andEqualTo("itemId", itemParamItem.getItemId());

        return mapper.updateByExampleSelective(itemParamItem, example);
    }
}
