package com.korat.search.controller;

import com.korat.search.bean.SearchResult;
import com.korat.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * searchcontroller
 *
 * @author solar
 * @date 2017/6/29
 */
@Controller("search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    private final Integer ROWS = 10;

    /**
     * 执行solr查询
     *
     * @param keyWords
     * @param page
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords, @RequestParam(value = "page",defaultValue = "0") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        SearchResult searchResult = null;
        searchResult = searchService.search(keyWords, page, ROWS);

    //    搜索关键字
        mv.addObject("query", keyWords);
        mv.addObject("itemList", searchResult.getResult());
        mv.addObject("page", page);
    //    总页数
        int total = searchResult.getTotal().intValue();
        int pages=total%ROWS==0?total/ROWS:total/ROWS+1;
        mv.addObject("pages", pages);

        return mv;
    }
}
