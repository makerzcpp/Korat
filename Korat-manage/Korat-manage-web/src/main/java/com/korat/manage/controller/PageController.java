package com.korat.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通用页面跳转
 *
 * @author solar
 * @date 2017/6/12
 */
@Controller
@RequestMapping("page")
public class PageController {

    /**
     * 跳转到任意页面的方法
     * @param pageName
     * @return
     */
    @RequestMapping(value = "{pageName}",method = RequestMethod.GET)
    public String toPage(@PathVariable("pageName") String pageName){
        return pageName;
    }

    //@RequestMapping(value = "item-edit")
    //public String query
}
