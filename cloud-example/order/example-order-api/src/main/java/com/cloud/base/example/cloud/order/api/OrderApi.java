package com.cloud.base.example.cloud.order.api;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.cloud.order.param.OrderCreateParam;
import com.cloud.base.example.cloud.order.vo.OrderVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderApi {

    @PostMapping("/order/create")
    @ApiOperation("创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "OrderCreateParam", dataTypeClass = OrderCreateParam.class, name = "param", value = "参数"),
    })
    ServerResponse<OrderVo> createOrder(@RequestBody OrderCreateParam param) throws Exception;


}
