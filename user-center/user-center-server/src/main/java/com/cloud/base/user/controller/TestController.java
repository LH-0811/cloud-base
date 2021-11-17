package com.cloud.base.user.controller;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.cloud.base.core.modules.youji.code.annotation.EnableYouJi;
import com.cloud.base.core.modules.youji.code.annotation.YouJiTask;
import com.cloud.base.user.param.SysUserCreateParam;
import com.cloud.base.user.param.SysUserUpdateParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试
 *
 * @author lh0811
 * @date 2021/8/7
 */

@Slf4j
@Api(tags = "用户中心-测试")
@RestController
@RequestMapping("/test")
@EnableYouJi
public class TestController {

    @YouJiTask(taskName = "测试任务1",
            taskNo = "Task0001",
            corn = "0/5 * * * * ?",
            param = "name=123",
            enable = true)
    public void testYouJiTask1(String param) throws Exception {
        log.info("[酉鸡 Worker Task1]  !!!!!! 参数:{}", param);
    }

    @YouJiTask(taskName = "测试任务2",
            taskNo = "Task0002",
            corn = "0/2 * * * * ?",
            param = "test-param",
            enable = true)
    public void testYouJiTask2(String param) throws Exception {
        log.info("[酉鸡 Worker Task2]  !!!!!! 参数:{}", param);
    }

}


















