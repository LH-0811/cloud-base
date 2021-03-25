package com.cloud.base.example.cloud.order.controller;


import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.cloud.order.param.OrderCreateParam;
import com.cloud.base.example.cloud.order.repository.entity.TOrder;
import com.cloud.base.example.cloud.order.service.OrderService;
import com.cloud.base.example.cloud.order.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation("创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "OrderCreateParam", dataTypeClass = OrderCreateParam.class, name = "param", value = "参数"),
    })
    public ServerResponse<OrderVo> createOrder(@RequestBody OrderCreateParam param) throws Exception {
        log.info(">>>>>>>>>>>进入创建订单接口OrderController.createOrder>>>>>>>>>>>>>>>>>>>>>>");
        OrderVo order = orderService.createOrder(param);
        return ServerResponse.createBySuccess("创建成功",order);
    }

}
