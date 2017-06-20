package com.korat.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.bean.ItemCatResult;
import com.korat.manage.domain.Item;
import com.korat.manage.service.ItemCatService;
import com.korat.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 请求侧栏商品信息的controller
 *
 * @author solar
 * @date 2017/6/16
 */
@Controller
@RequestMapping("api/item")
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private ItemService itemService;
    /**
     * 查询前台首页侧栏商品信息
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cat")
    public ResponseEntity<ItemCatResult> queryItemCat() {
        try {
            ItemCatResult itemCatResult = itemCatService.queryAllToTree();
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 后台接口提供的服务
     * @param itemId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{itemId}")
    public ResponseEntity<Item> queryItemByItemId(@PathVariable("itemId") Long itemId) {
        try {
            Item item = this.itemService.queryById(itemId);
            if (item != null) {
                return ResponseEntity.ok(item);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    //@RequestMapping(method = RequestMethod.GET)
    //public ResponseEntity<String> queryItemCat(@RequestParam(value = "callback",required = false)String callback) {
    //    try {
    //        ItemCatResult itemCatResult = itemCatService.queryAllToTree();
    //        String result = objectMapper.writeValueAsString(itemCatResult);
    //        if (StringUtils.isEmpty(callback)) {
    //            //无需跨域
    //            return ResponseEntity.ok(result);
    //        }
    //        //要跨域
    //        return ResponseEntity.ok(callback + "(" + result + ")");
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //}
}
