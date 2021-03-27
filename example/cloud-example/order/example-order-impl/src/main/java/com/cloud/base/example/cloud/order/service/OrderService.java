package com.cloud.base.example.cloud.order.service;


import com.cloud.base.example.cloud.order.param.OrderCreateParam;
import com.cloud.base.example.cloud.order.param.OrderSetFinishParam;
import com.cloud.base.example.cloud.order.repository.entity.TOrder;
import com.cloud.base.example.cloud.order.vo.OrderVo;

public interface OrderService {
    // 创建订单
    OrderVo createOrder(OrderCreateParam param) throws Exception;

}
