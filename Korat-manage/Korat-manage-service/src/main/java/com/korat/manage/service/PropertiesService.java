package com.korat.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 向其他地方注入资源的类
 *
 * @author solar
 * @date 2017/6/14
 */
@Service
public class PropertiesService {
    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;

    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;


}

