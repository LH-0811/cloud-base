package com.cloud.base.example.cloud.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.example.cloud.order.param.OrderCreateParam;
import com.cloud.base.example.cloud.order.repository.entity.TOrder;
import com.cloud.base.example.cloud.order.service.ExampleAccountService;
import com.cloud.base.example.cloud.order.service.OrderService;
import com.cloud.base.example.cloud.order.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExampleAccountService accountService;



    // value 资源名称 ， 降级后的处理
    @SentinelResource(value = "res_order_hello", blockHandler = "blockHandler")
    @GetMapping("/hello")
    @ApiOperation("hello测试流控接口")
    public ServerResponse hello() throws Exception {
        ServerResponse serverResponse = accountService.hello();
        if (serverResponse.isSuccess()){
            return ServerResponse.createBySuccess("hello order success");
        }else {
            throw CommonException.create(serverResponse);
        }
    }



    // value 资源名称 ， 降级后的处理
    @PostMapping("/create")
    @ApiOperation("创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "MyToken", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "OrderCreateParam", dataTypeClass = OrderCreateParam.class, name = "param", value = "参数"),
    })
    public ServerResponse<OrderVo> createOrder(@Validated @RequestBody OrderCreateParam param,@RequestHeader(value = "MyToken", defaultValue = "") String token) throws Exception {
        log.info(">>>>>>>>>>>进入创建订单接口OrderController.createOrder>>>>>>>>>>>>>>>>>>>>>>");
        OrderVo order = orderService.createOrder(param,token);
        return ServerResponse.createBySuccess("创建成功", order);
    }


    // 降级后的自定义返回
    public ServerResponse blockHandler(BlockException e) {
        return ServerResponse.createByError("账户系统hello：系统繁忙，请稍后再试");
    }
}
