package com.korat.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.common.service.ApiService;
import com.korat.web.bean.Cart;
import com.korat.web.bean.User;
import com.korat.web.util.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 购物车业务层
 *
 * @author solar
 * @date 2017/7/6
 */
@Service(value = "ORDER_CARTSERVICE")
public class CartService {
    @Autowired
    private ApiService apiService;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 向后台系统查询
     *
     * @return
     */
    public List<Cart> queryCartList() {
        User user= UserThreadLocal.get();
        String url="cart.korat.com/service/api/cart/"+user.getId();
        try {
            String result = apiService.doGet(url);
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            return objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
