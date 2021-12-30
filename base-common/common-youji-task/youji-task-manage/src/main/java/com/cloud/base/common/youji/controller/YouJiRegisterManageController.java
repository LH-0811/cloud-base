package com.cloud.base.common.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.youji.code.param.YouJiWorkerRegisterTaskParam;
import com.cloud.base.common.youji.service.YouJiManageService;
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

import javax.validation.Valid;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/youji/task/manage")
@RestController
public class YouJiRegisterManageController {

    @Autowired
    private YouJiManageService youJiManageService;

    @PostMapping("/create")
    @ApiOperation("创建定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "YouJiWorkerRegisterTaskParam", dataTypeClass = YouJiWorkerRegisterTaskParam.class, name = "param", value = "参数")
    })
    public ServerResponse registerWorker(@Valid @RequestBody YouJiWorkerRegisterTaskParam param) throws Exception {
        log.info("接收到客户端请求 创建定时任务:param={}",  JSON.toJSON(JSON.toJSONString(param)));
        youJiManageService.registerWorker(param);
        return ServerResponse.createBySuccess("成功");
    }


}
