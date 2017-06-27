package com.korat.web.controller;

import com.korat.order.pojo.Order;
import com.korat.order.service.OrderService;
import com.korat.web.bean.Item;
import com.korat.web.bean.User;
import com.korat.web.service.ItemService;
import com.korat.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单Controller
 *
 * @author solar
 * @date 2017/6/27
 */
@Controller(value = "OrderController_web")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    private final String COOKIENAME="TOKEN";
    /**
     * 通過商品id查詢商品返回view
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = itemService.queryItemByItemId(itemId);
        if (item != null) {
            mv.addObject("item", item);
        }
        return mv;
    }

    /**
     * 頁面提交后，保存訂單，user從token中獲取
     * @param order
     * @param token
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order, @CookieValue(COOKIENAME) String token) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.queryUserByToken(token);
        if(user!=null) {
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
        }
        String orderId = orderService.submitOrder(order);
        if (StringUtils.isEmpty(orderId)) {
            result.put("status", 300);
        }else {
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }

    /**
     * 跳轉到成功頁
     * @param orderId
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView toSuccessPage(@RequestParam("id") String orderId) {
        ModelAndView mv = new ModelAndView("success");
        Order order = orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
}
