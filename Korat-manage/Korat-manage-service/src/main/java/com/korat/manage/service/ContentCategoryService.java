package com.korat.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.korat.manage.domain.ContentCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容分类表
 *
 * @author solar
 * @date 2017/6/18
 */
@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    @Autowired
    private Mapper<ContentCategory> mapper;


    /**
     * 新增内容分类的事务解决方法
     *
     * @param contentCategory
     * @param parentId
     * @return
     */
    public boolean saveContentCategory(ContentCategory contentCategory, Long parentId) {
        //1、保存新增的category
        Integer count1 = super.saveSelective(contentCategory);
        //    2、修改父节点
        if (StringUtils.isNotEmpty(parentId.toString())) {
            ContentCategory contentCategory1 = new ContentCategory();
            contentCategory1.setId(parentId);
            contentCategory1 = this.mapper.selectOne(contentCategory1);

            //如果父节点的isParent为0的话
            if (contentCategory1 != null && !contentCategory1.getIsParent()) {
                Example example = new Example(ContentCategory.class);
                example.or().andEqualTo("id", parentId);
                contentCategory1.setIsParent(true);
                Integer count2 = this.mapper.updateByExample(contentCategory1, example);
                return count1 == 1 && count2 == 1;
            }
        }
        return count1 > 0;
    }

    /**
     * 内容分类重命名
     *
     * @param contentCategory
     * @return
     */
    public Integer updateContentCategoryName(ContentCategory contentCategory) {
        return this.mapper.updateByPrimaryKeySelective(contentCategory);
    }

    /**
     * 删除节点
     * 1、递归删除这个节点下面的所有子节点,必须用到递归
     * 2、查询这个节点是否有兄弟节点，如果没有就把父节点的isParent设置为0，有的话不变
     *
     * @param id
     * @param parentId
     * @return
     */
    public boolean deleteContentCategory(Long id, Long parentId) {
        //节点的集合
        List<Object> ids = new ArrayList<>();
        ids.add(id);
        //    收集所有以这个节点为父节点的节点，包括曾父节点
        findAllSubNode(ids, id);
        Integer count1 = deleteByIds(ContentCategory.class, "id", ids);

        //2、查询这个节点是否有兄弟节点，如果没有就把父节点的isParent设置为0，有的话不变
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> list = queryListByWhere(record);
        if (list.size() == 0) {
            record.setParentId(null);
            record.setId(id);
            record = queryOne(record);
            record.setIsParent(false);
            Integer count2 = updateSelective(record);
            return count1==1&&count2==2;
        }
        return count1>0;
    }

    private void findAllSubNode(List<Object> ids, Long id) {
        ContentCategory record = new ContentCategory();
        record.setParentId(id);
        List<ContentCategory> list = queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            if (contentCategory.getIsParent()) {
                findAllSubNode(ids, contentCategory.getId());
            }
        }
    }
}
