package com.korat.manage.service;

import com.korat.manage.domain.Item;
import com.korat.manage.domain.ItemDesc;
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

    public Integer save(Item item, String desc) {
        try {//初始化信息
            item.setStatus((byte) 1);
            //出于安全考虑，把id设置为null,由数据库自增长得到
            item.setId(null);

            //保存商品信息，不含描述
            super.save(item);

            //创建itemDesc对象
            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemDesc(desc);
            itemDesc.setItemId(item.getId());
            itemDescService.save(itemDesc);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
