package com.cloud.base.core.modules.rooster.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.rooster.code.param.RoosterTaskCreateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/rooster/task/worker")
@RestController
public class RoosterWorkerEchoController {

    @GetMapping("/heart_beat")
    @ApiOperation("酉鸡 工作节点的echo心跳检测接口")
    public ServerResponse registerWorker() throws Exception {
        return ServerResponse.createBySuccess("Rooster-Worker is ready to GO!");
    }

}
