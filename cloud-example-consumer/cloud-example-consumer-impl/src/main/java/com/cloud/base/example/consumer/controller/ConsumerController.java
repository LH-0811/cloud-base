package com.cloud.base.example.consumer.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.consumer.feign.ExampleProvdierApiClient;
import com.cloud.base.example.provider.param.ProviderQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lh0811
 * @date 2021/3/23
 */
@Slf4j
@Api(tags = {"消费者测试接口"})
@RestController
public class ConsumerController {

    @Autowired
    private ExampleProvdierApiClient provdierApiClient;


    @PostMapping("/consumer/query")
    @ApiOperation("提供者查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "ProviderQueryParam", dataTypeClass = ProviderQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse query(@RequestBody ProviderQueryParam param) throws Exception {
        log.info(">>>> 进入 ConsumerController.query:{}", JSON.toJSONString(param));
        ServerResponse serverResponse = provdierApiClient.query(param);
        return serverResponse;
    }

}
