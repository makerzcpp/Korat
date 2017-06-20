package com.korat.web.controller;

import com.korat.manage.domain.ItemDesc;
import com.korat.web.bean.Item;
import com.korat.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 商品详情页
 *
 * @author solar
 * @date 2017/6/20
 */
@Controller
@RequestMapping(value = "item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, value = "{itemId}")
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        Item item = itemService.queryItemByItemId(itemId);
       // ItemDesc itemDesc = itemService.queryItemParamByItemId(itemId);
        mv.addObject("item", item);
        //mv.addObject("itemDesc",itemDesc);
        return mv;
    }
}
