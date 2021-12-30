package com.cloud.base.common.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.ip.StringUtils;
import com.cloud.base.common.youji.code.param.YouJiWorkerReceiveTaskParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Method;

@Slf4j
@Api(tags = "酉鸡-任务接收接口")
@RequestMapping("/youji/task/worker")
@RestController
public class YouJiWorkerTaskReceiveController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/receive")
    @ApiOperation("酉鸡 工作节点接收任务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "YouJiWorkerReceiveTaskParam", dataTypeClass = YouJiWorkerReceiveTaskParam.class, name = "param", value = "参数")
    })
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
            return serverResponse;
        } catch (Exception e) {
            return ServerResponse.createByError("定时任务执行失败:"+e.getLocalizedMessage());
        }

    }

}
