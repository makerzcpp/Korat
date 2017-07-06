package com.korat.cart.controller;

import com.korat.cart.bean.Cart;
import com.korat.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 接受前台系统的请求
 *
 * @author solar
 * @date 2017/7/6
 */
@Controller
@RequestMapping("/api/cart/")
public class ApiCartController {
    @Autowired
    private CartService cartService;

    /**
     * 通过用户id查询购物车
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable("userId") Long userId) {
        List<Cart> cartList = cartService.queryCartListByUserId(userId);
        if (cartList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cartList);
    }

}
