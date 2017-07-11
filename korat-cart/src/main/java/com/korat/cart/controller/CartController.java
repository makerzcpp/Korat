package com.korat.cart.controller;

import com.korat.cart.bean.Cart;
import com.korat.cart.bean.User;
import com.korat.cart.service.CartCookieService;
import com.korat.cart.service.CartService;
import com.korat.cart.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private CartCookieService cartCookieService;

    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            this.cartCookieService.addItemToCart(itemId, request, response);
        } else {
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 查询购物车商品信息
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView queryCart(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        User user = UserThreadLocal.get();
        if (user == null) {
            List<Cart> cartList = this.cartCookieService.queryCartList(request);
            mv.addObject("cartList", cartList);
        } else {
            List<Cart> cartList = cartService.queryCartList();
            mv.addObject("cartList", cartList);
        }
        return mv;
    }

    /**
     * 更新商品数量
     *
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateItemNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
                                              HttpServletRequest request, HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            this.cartCookieService.updateItemNum(itemId, num, request, response);
        } else {
            this.cartService.updateItemNum(itemId, num);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    /**
     * 删除购物车中的商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItemFromCart(@PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            this.cartCookieService.deleteItemFromCart(itemId, request, response);
        } else {
            this.cartService.deleteItemFromCart(itemId);
        }
        return "redirect:/cart/list.html";
    }


}
