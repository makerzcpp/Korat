package com.korat.manage.service;

import com.korat.common.bean.ItemCatData;
import com.korat.common.bean.ItemCatResult;
import org.springframework.stereotype.Service;
import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.korat.manage.domain.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;

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
        //最后封装的结果
        ItemCatResult itemCatResult = new ItemCatResult();
        //众二级目录
        //众一级目录
        List<ItemCatData> level = new ArrayList<>();

        Example example = new Example(ItemCat.class);
        example.or().andEqualTo("parentId", 0);
        //一级目录的子项的集合
        List<ItemCat> itemCatList = mapper.selectByExample(example);
        //一级目录url
        String url = null;
        //一级目录name
        String name = null;
        //遍历一级目录子项的集合
        for (ItemCat itemCat : itemCatList) {
            List<ItemCatData> level2 = new ArrayList<>();
            //封装一级目录
            ItemCatData itemCatData = new ItemCatData();
            //封装一级目录的id
            url = "/products/" + itemCat.getId() + ".html";
            name = "<a href='/products/" + itemCat.getId() + "'.html>" + itemCat.getName() + "</a>";
            itemCatData.setName(name);
            itemCatData.setUrl(url);
            //一级目录添加url和name，除了二级目录的list
            //level.add(itemCatData);

            //查询二级目录

            Example example1 = new Example(ItemCat.class);
            example1.or().andEqualTo("parentId", itemCat.getId());
            List<ItemCat> itemCatList1 = mapper.selectByExample(example1);
            String url1 = null;
            String name1 = null;

            ItemCatData itemCatData2 = null;
            //遍历二级目录
            for (ItemCat itemCat2 : itemCatList1) {
                //封装二级目录
                itemCatData2 = new ItemCatData();
                url1 = "/products/" + itemCat2.getId();
                name1 = itemCat2.getName();
                itemCatData2.setUrl(url1);
                itemCatData2.setName(name1);
                //itemCatDataList.add(itemCatData2);

                //查询三级目录
                List<String> level3 = new ArrayList<>();

                Example example2 = new Example(ItemCat.class);
                example2.or().andEqualTo("parentId", itemCat2.getId());
                List<ItemCat> itemCatList3 = mapper.selectByExample(example2);
                for (ItemCat itemCat3 : itemCatList3) {
                    //这个是三级目录
                    String ide = "/products/" + itemCat3.getId() + ".html|" + itemCat3.getName();
                    level3.add(ide);
                }
                //三级目录遍历完成后就向二级目录中添加
                itemCatData2.setItems(level3);
                //二级目录遍历完成后就像一级目录中添加
                level2.add(itemCatData2);
            }
            itemCatData.setItems(level2);
            level.add(itemCatData);
        }
        itemCatResult.setItemCats(level);
        return itemCatResult;
    }
}
