package com.korat.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.korat.common.bean.EasyUIResult;
import com.korat.manage.domain.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 内容表
 *
 * @author solar
 * @date 2017/6/18
 */
@Service
public class ContentService extends BaseService<Content> {
    @Autowired
    private Mapper<Content> mapper;

    /**
     * 根据categoryId进行分页
     * @param category
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryListBycategoryId(Long category, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Content content = new Content();
        content.setCategoryId(category);
        Example example = new Example(Content.class);
        example.or().andEqualTo("categoryId", category);
        example.setOrderByClause("updated DESC");
        List<Content> contentList=this.mapper.selectByExample(example);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contentList);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

}
