package com.korat.manage.controller;

import com.korat.common.bean.EasyUIResult;
import com.korat.manage.domain.ItemParam;
import com.korat.manage.domain.ItemParamItem;
import com.korat.manage.service.ItemParamItemService;
import com.korat.manage.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品规格模板controller
 *
 * @author solar
 * @date 2017/6/15
 */
@RequestMapping(value = "item")
@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;
    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 查询数据库中有没有商品的规格参数
     *
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "param/{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {
        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            itemParam = itemParamService.queryOne(itemParam);
            if (itemParam != null) {
                return ResponseEntity.status(HttpStatus.OK).body(itemParam);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 保存商品的规格参数
     *
     * @param paramData
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "param/{itemCatId}")
    public ResponseEntity<Void> saveItemParam(@RequestParam("paramData") String paramData, @PathVariable("itemCatId") Long itemCatId) {
        ItemParam itemParam = new ItemParam();
        itemParam.setParamData(paramData);
        itemParam.setItemCatId(itemCatId);

        itemParamService.saveSelective(itemParam);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * 查询商品规格参数
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "param/list")
    public ResponseEntity<EasyUIResult> queryItemParam(@RequestParam(value = "page") Integer page,
                                                       @RequestParam(value = "rows") Integer rows) {
        try {
            EasyUIResult easyUIResult = itemParamService.queryItemList(page, rows);
            if (easyUIResult != null) {
                return ResponseEntity.ok(easyUIResult);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 通过商品id查询是否有对应的规格描述
     * @param itemId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "param/item/query/{itemId}")
    public ResponseEntity<ItemParamItem> queryItemParamByItemId(@PathVariable("itemId") Long itemId) {
        try {
            ItemParamItem itemParamItem = itemParamItemService.queryById(itemId);
            if (itemParamItem != null) {
                return ResponseEntity.ok(itemParamItem);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
