package com.korat.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.manage.service.PicUploadResult;
import com.korat.manage.service.PropertiesService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 图片控制类
 *
 * @author solar
 * @date 2017/6/14
 */
@Controller
@RequestMapping(value = "/pic")
public class PicController {

    @Autowired
    private PropertiesService propertiesService;

    private static final ObjectMapper mapper = new ObjectMapper();
    //允许上传的格式
    private static final String[] IMAGE_TYPE = {".bmp", ".jpg", ".jpeg", ".png", ".gif"};

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile file, HttpServletResponse response) throws IOException {
        boolean isLegal = false;
        //1、校验图片的格式是否正确，正确的则isLegal为true
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        //    封装PicUploadResult对象
        PicUploadResult picUploadResult = new PicUploadResult();
        picUploadResult.setError(isLegal ? 0 : 1);

        //    文件新路径,参数为文件名
        String filePath = getFilePath(file.getOriginalFilename());

        //    生成图片的绝对引用地址
        String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, propertiesService.REPOSITORY_PATH), "\\", "/");
        //solartodo 会添加图片服务系统
        picUploadResult.setUrl(propertiesService.IMAGE_BASE_URL + picUrl);

        File newFile = new File(filePath);

        //    写文件到磁盘
        file.transferTo(newFile);

        //判断图片是否合法
        isLegal = false;
        try {
            BufferedImage image = ImageIO.read(newFile);
            if (image != null) {
                picUploadResult.setWidth(image.getWidth() + "");
                picUploadResult.setHigh(image.getHeight() + "");
                isLegal = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //再次设定状态
        picUploadResult.setError(isLegal ? 0 : 1);
        //不合法则删除之
        if (!isLegal) {
            newFile.delete();
        }
        response.setContentType(MediaType.TEXT_HTML_VALUE);

        return mapper.writeValueAsString(picUploadResult);
    }

    private String getFilePath(String originalFilename) {
        String baseFolder = propertiesService.REPOSITORY_PATH + File.separator + "images";
        Date nowDate = new Date();
        // yyyy/MM/dd
        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy") + File.separator + new DateTime(nowDate).toString("MM") + File.separator
                + new DateTime(nowDate).toString("dd");
        File file = new File(fileFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目录
            file.mkdirs();
        }
        // 生成新的文件名
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS") + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(originalFilename, ".");
        return fileFolder + File.separator + fileName;
    }

}

