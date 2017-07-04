package com.korat.cart.controller;

import com.korat.cart.bean.Cart;
import com.korat.cart.bean.User;
import com.korat.cart.service.CartService;
import com.korat.cart.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 购物车控制类
 *
 * @author solar
 * @date 2017/7/4
 */
@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId) {
        User user= UserThreadLocal.get();
        if (user == null) {

        }else {
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView queryCartList() {
        ModelAndView mv = new ModelAndView("cart");
        User user=UserThreadLocal.get();
        if (user == null) {

        }else {
            List<Cart> cartList = cartService.queryCartList();
            mv.addObject("cartList", cartList);
        }
        return mv;
    }
}
