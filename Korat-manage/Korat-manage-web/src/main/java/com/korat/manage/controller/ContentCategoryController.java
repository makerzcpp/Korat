package com.korat.manage.controller;

import com.korat.manage.domain.ContentCategory;
import com.korat.manage.service.ContentCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 内容分类controller
 *
 * @author solar
 * @date 2017/6/18
 */
@Controller
@RequestMapping(value = "content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询内容分类
     *
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategory(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        try {
            ContentCategory contentCategory = new ContentCategory();
            contentCategory.setParentId(parentId);
            List<ContentCategory> contentCategoryList = contentCategoryService.queryListByWhere(contentCategory);
            if (contentCategoryList != null) {
                return ResponseEntity.ok(contentCategoryList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增内容分类
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(@RequestParam(value = "parentId") Long parentId,
        @RequestParam(value = "name") String name){
        try{
            ContentCategory contentCategory = new ContentCategory();
            if (StringUtils.isNotEmpty(parentId.toString())) {
                contentCategory.setParentId(parentId);
            }
            if (StringUtils.isNotEmpty(name)) {
                contentCategory.setName(name);
            }
            contentCategory.setId(null);
            contentCategory.setStatus(1);
            contentCategory.setSortOrder(1);
            contentCategory.setIsParent(false);
            boolean flag=contentCategoryService.saveContentCategory(contentCategory,parentId);

            if (flag) {
                return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 内容分类重命名
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateContentCategoryName(@RequestParam(value = "id") Long id, @RequestParam(value = "name") String name) {
        try {
            ContentCategory contentCategory = new ContentCategory();
            if (StringUtils.isNotEmpty(id.toString())) {
                contentCategory.setId(id);
            }
            if (StringUtils.isNotEmpty(name)) {
                contentCategory.setName(name);
            }
            Integer count = contentCategoryService.updateContentCategoryName(contentCategory);
            if (count > 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除节点
     * 1、递归删除这个节点下面的所有子节点
     * 2、查询这个节点是否有兄弟节点，如果没有就把父节点的isParent设置为0，有的话不变
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory) {
        try {

            boolean count = contentCategoryService.deleteContentCategory(contentCategory.getId(), contentCategory.getParentId());
            if (count) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
