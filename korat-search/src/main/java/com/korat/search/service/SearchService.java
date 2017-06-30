package com.korat.search.service;

import com.korat.search.bean.Item;
import com.korat.search.bean.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author solar
 * @date 2017/6/29
 */
@Service
public class SearchService {
    @Autowired
    private HttpSolrServer httpSolrServer;
    public SearchResult search(String keyWords, Integer page, Integer rows) {
        //构造搜索条件
        SolrQuery solrQuery = new SolrQuery();
    //    搜索关键字
        solrQuery.setQuery("title:" + keyWords + " AND status:1");
    //    设置分页,start表示从多少开始
        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
    //    rows表示从start开始的条数
        solrQuery.setRows(rows);
    //    设置是否需要高亮,keyWords不等于空并且不等于*
        boolean isHighLighting = !StringUtils.equals("*", keyWords) && StringUtils.isNotEmpty(keyWords);
        if (isHighLighting) {
        //    开启高亮
            solrQuery.setHighlight(true);
        //    高亮的字段
            solrQuery.addHighlightField("title");
        //    设置高亮的形式
            solrQuery.setHighlightSimplePre("<em>");
            solrQuery.setHighlightSimplePost("</em>");

            try {
                QueryResponse queryResponse = httpSolrServer.query(solrQuery);
            //    转换成Item类型
                List<Item> items = queryResponse.getBeans(Item.class);
                if (isHighLighting) {
                //    将高亮的标题数据写会到数据对象中
                    Map<String,Map<String,List<String>>> map=queryResponse.getHighlighting();
                    for (Map.Entry<String, Map<String, List<String>>> hightlighting : map.entrySet()) {
                        for (Item item : items) {
                            if (!hightlighting.getKey().equals(item.getId().toString())) {
                                continue;
                            }
                            item.setTitle(StringUtils.join(hightlighting.getValue().get("title"), ""));
                            break;
                        }
                    }
                }
                return new SearchResult(queryResponse.getResults().getNumFound(), items);
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
