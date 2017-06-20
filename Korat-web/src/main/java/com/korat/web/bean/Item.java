package com.korat.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * 继承korat.manage.pojo中的Item
 *
 * @author solar
 * @date 2017/6/20
 */
public class Item extends com.korat.manage.domain.Item {
    public String[] getImages() {
        return StringUtils.split(super.getImage(), ",");
    }
}
