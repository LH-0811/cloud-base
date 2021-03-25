package com.cloud.base.example;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.logger.annotation.LhitLogger;
import com.cloud.base.core.modules.logger.entity.LoggerBusinessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试统一异常处理 和 基础返回信息封装类
 *
 * @author lh0811
 * @date 2021/3/21
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    @LhitLogger(title = "测试hello",businessType = LoggerBusinessType.QUERY)
    @GetMapping("/hello")
    public String hello() throws Exception {
        return "hello world";
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
//        int a = 1/0;  打开就走全局异常 不打开就走自定义统一异常

        throw CommonException.create(ServerResponse.createByError("测试统一异常处理"));
    }

}
