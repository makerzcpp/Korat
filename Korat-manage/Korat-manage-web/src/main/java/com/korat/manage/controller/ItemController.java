package com.korat.manage.controller;

import com.korat.manage.domain.EasyUIResult;
import com.korat.manage.domain.Item;
import com.korat.manage.domain.ItemDesc;
import com.korat.manage.service.ItemDescService;
import com.korat.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 商品控制类
 *
 * @author solar
 * @date 2017/6/14
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;

    /**
     * 新增商品
     *
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        try {
            if (StringUtils.isBlank(item.getTitle())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                Boolean flag = itemService.save(item, desc,itemParams);
                if (flag) {
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }


    /**
     * 分页信息
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        try {
            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品描述回显
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "desc/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDesc(@PathVariable("itemId") Long itemId) {
        try {
            ItemDesc itemDesc = itemDescService.queryById(itemId);
            if (itemDesc != null) {
                return ResponseEntity.ok(itemDesc);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        try {
            //    判断参数是否正确
            if (StringUtils.isEmpty(item.getTitle()) || StringUtils.length(item.getTitle()) > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            //    保存商品
            boolean isUpdate = itemService.updateItem(item, desc,itemParams);
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
