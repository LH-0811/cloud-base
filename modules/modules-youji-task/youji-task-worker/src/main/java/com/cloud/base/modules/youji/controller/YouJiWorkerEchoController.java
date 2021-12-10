package com.cloud.base.modules.youji.controller;

import com.cloud.base.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/youji/task/worker")
@RestController
public class YouJiWorkerEchoController {

    @GetMapping("/heart_beat")
    @ApiOperation("酉鸡 工作节点的echo心跳检测接口")
    public ServerResponse registerWorker() throws Exception {
        return ServerResponse.createBySuccess("YouJi-Worker is ready to GO!");
    }

}
