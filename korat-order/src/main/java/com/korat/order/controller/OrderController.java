package com.korat.order.controller;
import com.korat.order.bean.KoratResult;
import com.korat.order.pojo.Order;
import com.korat.order.pojo.PageResult;
import com.korat.order.pojo.ResultMsg;
import com.korat.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@RequestMapping("/order")
@Controller(value = "OrderController_order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	
	/**
	 * 创建订单
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create" , method = RequestMethod.POST)
	public KoratResult createOrder(@RequestBody String json) {
		return orderService.createOrder(json);
	}
	
	
	/**
	 * 根据订单ID查询订单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/query/{orderId}" ,method = RequestMethod.GET)
	public Order queryOrderById(@PathVariable("orderId") String orderId) {
		return orderService.queryOrderById(orderId);
	}

	/**
	 * 根据用户名分页查询订单
	 * @param buyerNick
	 * @param page
	 * @param count
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query/{buyerNick}/{page}/{count}")
	public PageResult<Order> queryOrderByUserNameAndPage(@PathVariable("buyerNick") String buyerNick, @PathVariable("page") Integer page, @PathVariable("count") Integer count) {
		return orderService.queryOrderByUserNameAndPage(buyerNick, page, count);
	}

	
	/**
	 * 修改订单状态
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changeOrderStatus",method = RequestMethod.POST)
	public ResultMsg changeOrderStatus(@RequestBody String json) {
		return orderService.changeOrderStatus(json);
	}


}
