package com.korat.web.controller;

import com.korat.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 跳转方法
 *
 * @author solar
 * @date 2017/6/16
 */
@Controller
public class IndexController {
    @Value("${INDEX_AD1_URL}")
    private  String INDEX_AD1_URL;

    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;

    @Autowired
    private IndexService indexService;
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        String image = indexService.queryIndexAd1(INDEX_AD1_URL);
        String image2 = indexService.queryIndexAd1(INDEX_AD2_URL);

        mv.addObject("indexAD1", image);
        mv.addObject("indexAD2", image2);

        return mv;
    }


}
