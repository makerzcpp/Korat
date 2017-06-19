package com.korat.manage.controller;

import com.korat.common.bean.EasyUIResult;
import com.korat.manage.domain.Content;
import com.korat.manage.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 内容controller
 *
 * @author solar
 * @date 2017/6/18
 */
@Controller
@RequestMapping(value = "content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 根据categoryId进行分页
     * @param page
     * @param rows
     * @param category
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentByPage(@RequestParam(value = "page",defaultValue = "0")Integer page
                        , @RequestParam(value = "rows",defaultValue = "10")Integer rows
                        ,@RequestParam(value = "categoryId")Long category){
        try {
            EasyUIResult easyUIResult = contentService.queryListBycategoryId(category,page, rows);
            if (easyUIResult != null) {
                return ResponseEntity.ok(easyUIResult);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增内容
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {
            content.setId(null);
            Integer count = contentService.save(content);
            if (count > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
