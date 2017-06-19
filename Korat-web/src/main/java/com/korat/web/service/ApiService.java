package com.korat.web.service;

import com.korat.web.httpclient.HttpResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装httpclient的各种方法
 *
 * @author solar
 * @date 2017/6/19
 */
@Service
public class ApiService implements BeanFactoryAware {
    private BeanFactory beanFactory;
    @Autowired
    private RequestConfig requestConfig;

    /**
     * get请求，成功返回string，失败返回null
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String doGet(String url) throws IOException {
        //创建GET请求
        HttpGet httpGet = new HttpGet(url);
        //设置参数
        httpGet.setConfig(requestConfig);
        //创建响应
        CloseableHttpResponse response = null;

        try {
            response = getClient().execute(httpGet);
            //    判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 带参数的get方法
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public String doGetParam(String url, Map<String, String> params) throws URISyntaxException, IOException {
        //    定义请求参数
        URIBuilder builder = new URIBuilder(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }
        return this.doGet(builder.build().toString());
    }

    /**
     * post
     *
     * @param url
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        return this.doPostParam(url, null);

    }

    /**
     * 带参数的post
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public HttpResult doPostParam(String url, Map<String, String> param) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        if (param != null) {
            //    设置参数
            List<NameValuePair> parameters = new ArrayList<>(0);
            for (Map.Entry<String, String> entry : param.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            //    构造一个from表单的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            //    将请求实体设置到httpPost表单
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;

        try {
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    private CloseableHttpClient getClient() {
        return beanFactory.getBean(CloseableHttpClient.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
