package com.cloud.base.user.controller;

import com.cloud.base.modules.youji.code.annotation.EnableYouJi;
import com.cloud.base.modules.youji.code.annotation.YouJiTask;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 测试
 *
 * @author lh0811
 * @date 2021/8/7
 */

@Slf4j
@Api(tags = "用户中心-酉鸡定时任务管理测试")
@RestController
@RequestMapping("/youji")
@EnableYouJi
public class YouJiController {

    @YouJiTask(taskName = "测试任务1",
            taskNo = "Task0001",
            corn = "0/5 * * * * ?",
            param = "name=123",
            enable = false)
    public void testYouJiTask1(String param) throws Exception {
        log.info("[酉鸡 Worker Task1]  !!!!!! 参数:{}", param);
    }

    @YouJiTask(taskName = "测试任务2",
            taskNo = "Task0002",
            corn = "0/2 * * * * ?",
            param = "test-param",
            enable = false)
    public void testYouJiTask2(String param) throws Exception {
        log.info("[酉鸡 Worker Task2]  !!!!!! 参数:{}", param);
    }

}


















