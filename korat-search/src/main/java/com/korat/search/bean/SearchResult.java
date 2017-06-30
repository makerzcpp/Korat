package com.korat.search.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * solr搜索结果类型
 *
 * @author solar
 * @date 2017/6/29
 */
public class SearchResult {

    public SearchResult(Long total, List<Item> result) {
        this.total = total;
        this.result = result;
    }

    //    总页数
    private long total;
    //    结果
    private List<Item> result = new ArrayList<>(0);

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Item> getResult() {
        return result;
    }

    public void setResult(List<Item> result) {
        this.result = result;
    }
}
