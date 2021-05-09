package com.cloud.base.logger;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.logger.annotation.LhitLogger;
import com.cloud.base.core.modules.logger.entity.LoggerBusinessType;
import com.cloud.base.logger.param.ExampleParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 测试统一异常处理 和 基础返回信息封装类
 *
 * @author lh0811
 * @date 2021/3/21
 */
@RestController
@RequestMapping("/logger")
@Api(tags = "测试基础功能和日志功能")
public class LoggerController {

    @LhitLogger(title = "测试hello", businessType = LoggerBusinessType.QUERY)
    @GetMapping("/hello")
    public String hello() throws Exception {
        return "hello world:"+Thread.currentThread().getId();
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
//        int a = 1/0;  打开就走全局异常 不打开就走自定义统一异常
        throw CommonException.create(ServerResponse.createByError("测试统一异常处理"));
    }

    @PostMapping("/name")
    @ApiOperation(value = "测试入参合法性")
    public ServerResponse nameTest(@Validated @RequestBody ExampleParam param) throws Exception {
        return ServerResponse.createBySuccess("校验通过",param);
    }

}
