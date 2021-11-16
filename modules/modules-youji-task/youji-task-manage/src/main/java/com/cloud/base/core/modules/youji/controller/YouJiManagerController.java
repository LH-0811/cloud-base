package com.cloud.base.core.modules.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerRegisterTaskParam;
import com.cloud.base.core.modules.youji.service.YouJiTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/rooster/task/manage")
@RestController
public class YouJiManagerController {

    @Autowired
    private YouJiTaskService youJiTaskService;

    @PostMapping("/create")
    @ApiOperation("创建定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoosterTaskCreateParam", dataTypeClass = YouJiWorkerRegisterTaskParam.class, name = "param", value = "参数")
    })
    public ServerResponse registerWorker(@Valid @RequestBody YouJiWorkerRegisterTaskParam param, HttpServletRequest request) throws Exception {
        log.info("接收到客户端请求 创建定时任务:Local  addr={},port={},param={}", request.getLocalAddr(), request.getLocalPort(), JSON.toJSON(JSON.toJSONString(param)));
        log.info("接收到客户端请求 创建定时任务:Remote addr={}, host={},port={},param={}", request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort(), JSON.toJSON(JSON.toJSONString(param)));
        youJiTaskService.registerWorker(param,request.getLocalAddr(),request.getLocalPort());
        return ServerResponse.createBySuccess("成功");
    }
}
