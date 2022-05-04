package com.cloud.base.common.youji.cronjob.mgr.controller;

import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.youji.cronjob.core.param.Youji2TaskRegisterParam;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lh0811
 * @date 2022/4/16
 */
@Slf4j
@RestController
@RequestMapping("/youji/mgr")
public class Youji2Controller {

    @Autowired
    private Youji2MgrService youji2MgrService;

    @GetMapping("/echo")
    public ServerResponse echo() throws Exception {
        return ServerResponse.createBySuccess("success");
    }


    @PostMapping("/task/register")
    public ServerResponse receiveTask(@Valid @RequestBody Youji2TaskRegisterParam param) throws Exception {
        youji2MgrService.registerTask(param);
        return ServerResponse.createBySuccess("注册成功");
    }

}
