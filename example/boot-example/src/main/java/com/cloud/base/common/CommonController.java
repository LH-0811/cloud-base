package com.cloud.base.common;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author lh0811
 * @date 2021/4/14
 */
@Slf4j
@RestController("/common")
@Api(tags = "通用测试接口")
public class CommonController {


    @GetMapping("/test")
    public String test() {
        return  String.valueOf(Thread.currentThread().getId());
    }

}
