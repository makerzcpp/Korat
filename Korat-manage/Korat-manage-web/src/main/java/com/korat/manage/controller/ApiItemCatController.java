package com.korat.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.bean.ItemCatResult;
import com.korat.manage.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("api/item/cat")
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCat() {
        try {
            ItemCatResult itemCatResult = itemCatService.queryAllToTree();
            return ResponseEntity.ok(itemCatResult);
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
