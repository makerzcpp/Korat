package com.korat.manage.service;

/**
 * 图片上传结果验证
 *
 * @author solar
 * @date 2017/6/14
 */
public class PicUploadResult {
    private String width;
    private String high;
    private String url;
    //校验图片格式是否正确，0为正确，1为错误
    private Integer error;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
}
