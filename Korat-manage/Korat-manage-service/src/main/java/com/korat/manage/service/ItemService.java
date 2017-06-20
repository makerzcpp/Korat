package com.korat.manage.service;

import com.korat.manage.domain.Item;
import com.korat.manage.domain.ItemDesc;
import com.korat.manage.domain.ItemParam;
import com.korat.manage.domain.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * item业务层
 *
 * @author solar
 * @date 2017/6/14
 */
@Service
public class ItemService extends BaseService<Item> {
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 新增商品
     *
     * @param item
     * @param desc
     * @param itemParams
     * @return
     */
    public boolean save(Item item, String desc, String itemParams) {
        try {//初始化信息
            item.setStatus((byte) 1);
            //出于安全考虑，把id设置为null,由数据库自增长得到
            item.setId(null);

            //保存商品信息，不含描述
            Integer count1 = super.saveSelective(item);

            //创建itemDesc对象
            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemDesc(desc);
            itemDesc.setItemId(item.getId());
            Integer count2 = itemDescService.saveSelective(itemDesc);

            if (itemParams != null) {
                ItemParamItem itemParam = new ItemParamItem();
                itemParam.setId(item.getId());
                itemParam.setItemId(item.getCid());
                itemParam.setParamData(itemParams);
                Integer count3 = itemParamItemService.saveSelective(itemParam);
                return count1 == 1 && count2 == 1 && count3 == 1;
            }
            return count1 == 1 && count2 == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新商品
     *
     * @param item
     * @param desc
     * @return
     */
    public boolean updateItem(Item item, String desc, String itemParams) {
        item.setStatus(null);
        item.setId(null);

        //    更新商品,通过主键更新不为null的字段
        Integer count1 = super.updateSelective(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);

        Integer count2 = this.itemDescService.updateSelective(itemDesc);

        if (itemParams != null) {
            ItemParamItem itemParamItem = new ItemParamItem();
            itemParamItem.setId(null);
            itemParamItem.setItemId(item.getCid());
            itemParamItem.setParamData(itemParams);
            Integer count3 = this.itemParamItemService.updateItemParamsByExample(itemParamItem);
            return count1 == 1 && count2 == 1 && count3 == 1;
        }
        return count1 == 1 && count2 == 1;
    }


}
