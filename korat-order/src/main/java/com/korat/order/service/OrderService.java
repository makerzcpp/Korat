package com.korat.order.service;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.korat.common.httpclient.HttpResult;
import com.korat.common.service.ApiService;
import com.korat.order.bean.KoratResult;
import com.korat.order.dao.IOrder;
import com.korat.order.pojo.Order;
import com.korat.order.pojo.PageResult;
import com.korat.order.pojo.ResultMsg;
import com.korat.order.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${KORAT_ORDER_URL}")
    private String KORAT_ORDER_URL;
    @Autowired
    private IOrder orderDao;
    @Autowired
    private ApiService apiService;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    public KoratResult createOrder(String json) {
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
            // 校验Order对象
            ValidateUtil.validate(order);
        } catch (Exception e) {
            return KoratResult.build(400, "请求参数有误!");
        }

        try {
            // 生成订单ID，规则为：userid+当前时间戳
            String orderId = order.getUserId() + "" + System.currentTimeMillis();
            order.setOrderId(orderId);

            // 设置订单的初始状态为未付款
            order.setStatus(1);

            // 设置订单的创建时间
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());

            // 设置买家评价状态，初始为未评价
            order.setBuyerRate(0);

            // 持久化order对象
            orderDao.createOrder(order);

            //发送消息到RabbitMQ
//            Map<String, Object> msg = new HashMap<String, Object>(3);
//            msg.put("userId", order.getUserId());
//            msg.put("orderId", order.getOrderId());
//            List<Long> itemIds = new ArrayList<Long>(order.getOrderItems().size());
//            for (OrderItem orderItem : order.getOrderItems()) {
//                itemIds.add(orderItem.getItemId());
//            }
//            msg.put("itemIds", itemIds);
//            this.rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(msg));

            return KoratResult.ok(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return KoratResult.build(400, "保存订单失败!");
    }

    public Order queryOrderById(String orderId) {
        Order order = orderDao.queryOrderById(orderId);
        return order;
    }

    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, int page, int count) {
        return orderDao.queryOrderByUserNameAndPage(buyerNick, page, count);
    }

    public ResultMsg changeOrderStatus(String json) {
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMsg("400", "请求参数有误!");
        }
        return this.orderDao.changeOrderStatus(order);
    }

    /**
     * 提交訂單
     *
     * @param order
     * @return
     */
    public String submitOrder(Order order) {
        if (order == null) {
            return null;
        }
        String url = KORAT_ORDER_URL + "/order/create";
        try {
            HttpResult httpResult = apiService.doPostJson(url, objectMapper.writeValueAsString(order));
            if (httpResult.getCode() == 200) {
                String data = httpResult.getData();
                JsonNode jsonNode = objectMapper.readTree(data);
                if (jsonNode.get("status").intValue() == 200) {
                    return jsonNode.get("data").asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
