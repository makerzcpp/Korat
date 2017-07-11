package com.korat.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.cart.bean.Cart;
import com.korat.cart.bean.Item;
import com.korat.cart.mapper.CartMapper;
import com.korat.cart.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author solar
 * @date 2017/7/5
 */
@Service
public class CartCookieService {
    private static final String COOKIE_NAME = "ITEM_TOKEN";
    private static final Integer COOKIE_MAX_AGE = 60 * 60 * 24 * 30 * 12;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @param request
     * @param response
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> cartList = queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId) {
                cart = c;
            }
        }
        //购物车中没有指定的商品
        if (cart == null) {
            Item item = this.itemService.queryItemByItemId(itemId);
            cart=new Cart();
            cart.setItemId(itemId);
            cart.setNum(1);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cartList.add(cart);
        } else {
            //    购物车中这个商品
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
        }
        saveCartToCookie(cartList, request, response);
    }

    /**
     * 添加商品列表到cookie中
     *
     * @param cartList
     * @param request
     * @param response
     */
    public void saveCartToCookie(List<Cart> cartList, HttpServletRequest request, HttpServletResponse response) {
        try {
            String cookieValue = objectMapper.writeValueAsString(cartList);
            CookieUtils.setCookie(request, response, COOKIE_NAME, cookieValue, true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取购物车中的商品
     *
     * @param request
     * @return
     */
    public List<Cart> queryCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_NAME,"UTF-8");
        if (StringUtils.isEmpty(cookieValue)) {
            return new ArrayList<>(0);
        }
        try {
            return objectMapper.readValue(cookieValue, objectMapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    /**
     * 更新商品信息
     *
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    public void updateItemNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        //    说明购物车中已经有商品了，就不用再查了
        List<Cart> cartList = queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId) {
                cart = c;
                cart.setNum(num);
                cart.setUpdated(new Date());
                break;
            }
        }
        saveCartToCookie(cartList, request, response);

    }

    /**
     * 删除购物车中的商品
     *
     * @param itemId
     * @param request
     */
    public void deleteItemFromCart(Long itemId, HttpServletRequest request,HttpServletResponse response) {
        List<Cart> cartList = queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId) {
                cart = c;
                cartList.remove(cart);
                break;
            }
        }
        saveCartToCookie(cartList, request, response);
    }
}
