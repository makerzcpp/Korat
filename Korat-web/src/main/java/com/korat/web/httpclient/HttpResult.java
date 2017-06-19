package com.korat.web.httpclient;

/**
 * post请求返回的结果
 *
 * @author solar
 * @date 2017/6/19
 */
public class HttpResult {
    Integer code;
    String data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HttpResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public HttpResult() {

    }
}
