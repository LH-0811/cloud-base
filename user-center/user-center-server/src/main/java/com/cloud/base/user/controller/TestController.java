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

    @YouJiTask(taskName = "测试任务1", taskNo = "Task0001", corn = "0/5 * * * * ?", enable = true)
    public void testRoosterTask1(SysUserCreateParam param1) throws Exception {

    }

    @YouJiTask(taskName = "测试任务2", taskNo = "Task0002", corn = "0/2 * * * * ?", enable = true)
    public void testRoosterTask2(SysUserCreateParam param1, SysUserUpdateParam param2) throws Exception {

    }

    @GetMapping("/test1")
    public String test() throws Exception {
        NamingService naming = NamingFactory.createNamingService("127.0.0.1:8848");
        List<Instance> allInstances = naming.getAllInstances("user-center-server");
        System.out.println(naming.getAllInstances("user-center-server"));
        return "success";
    }


}


















