package com.cloud.base.core.modules.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.ip.StringUtils;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Api(tags = "酉鸡-任务接收接口")
@RequestMapping("/rooster/task/worker")
@RestController
public class YouJiWorkerTaskReceiveController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/receive")
    @ApiOperation("酉鸡 工作节点接收任务接口")
    public ServerResponse receiveWorker(@RequestBody YouJiWorkerReceiveTaskParam param) throws Exception {
        log.info("[酉鸡 Worker 接收到执行任务] param={}", JSON.toJSONString(param));
        Object bean = applicationContext.getBean(Class.forName(param.getTaskBeanName()));
        Method method = null;
        if (StringUtils.isBlank(param.getTaskParam())){
            method = bean.getClass().getMethod(param.getTaskMethod());
        }else {
            method = bean.getClass().getMethod(param.getTaskMethod(),String.class);
        }
        Object invoke = method.invoke(bean, param.getTaskParam());
        return ServerResponse.createBySuccess("Rooster-Worker is ready to GO!");
    }

}
