package com.korat.order.mapper;

import java.util.Date;


import com.korat.order.pojo.Order;
import org.apache.ibatis.annotations.Param;


public interface OrderMapper extends IMapper<Order>{
	
	public void paymentOrderScan(@Param("date") Date date);

}
