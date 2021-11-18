package com.cloud.base.core.modules.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.ip.StringUtils;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;
import com.cloud.base.core.modules.youji.service.YouJiWorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Api(tags = "酉鸡-任务接收接口")
@RequestMapping("/youji/task/worker")
@RestController
public class YouJiWorkerTaskReceiveController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private YouJiWorkerService youJiWorkerService;

    @PostMapping("/receive")
    @ApiOperation("酉鸡 工作节点接收任务接口")
    public ServerResponse receiveWorker(@Valid @RequestBody YouJiWorkerReceiveTaskParam param) throws Exception {
        log.info("[酉鸡 Worker 接收到执行任务] param={}", JSON.toJSONString(param));
        try {
            ServerResponse serverResponse = null;
            Object bean = applicationContext.getBean(Class.forName(param.getTaskBeanName()));
            Method method = null;
            if (StringUtils.isBlank(param.getTaskParam())) {
                method = bean.getClass().getMethod(param.getTaskMethod());
            } else {
                method = bean.getClass().getMethod(param.getTaskMethod(), String.class);
            }
            if (method == null) {
                serverResponse = ServerResponse.createByError("工作节点定时任务不存在: " + param.getTaskBeanName() + "." + param.getTaskMethod() + " hasParam=" + org.apache.commons.lang3.StringUtils.isNotBlank(param.getTaskParam()));
            } else {
                Object invoke = method.invoke(bean, param.getTaskParam());
                if (invoke instanceof ServerResponse) {
                    serverResponse = (ServerResponse) invoke;
                } else {
                    serverResponse = ServerResponse.createBySuccess("执行成功");
                }
            }
            // 工作节点完成工作后 增加日志
            youJiWorkerService.finishTask(param, serverResponse);
            return serverResponse;
        } catch (Exception e) {
            return ServerResponse.createByError("定时任务执行失败:"+e.getLocalizedMessage());
        }

    }

}
