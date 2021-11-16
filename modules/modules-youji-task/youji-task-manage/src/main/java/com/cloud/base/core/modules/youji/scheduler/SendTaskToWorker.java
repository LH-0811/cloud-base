package com.cloud.base.core.modules.youji.scheduler;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.modules.youji.code.constant.YouJiConstant;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.util.YouJiOkHttpClientUtil;
import com.cloud.base.core.modules.youji.service.YouJiTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class SendTaskToWorker implements Runnable {

    private YouJiTaskService youJiTaskService;
    private YouJiOkHttpClientUtil httpClientUtil;
    private TaskInfo taskInfo;

    public SendTaskToWorker(TaskInfo taskInfo,YouJiTaskService youJiTaskService,YouJiOkHttpClientUtil httpClientUtil) {
        this.taskInfo = taskInfo;
        this.httpClientUtil = httpClientUtil;
        this.youJiTaskService = youJiTaskService;
    }

    @Override
    public void run() {
        log.info("[酉鸡 Manage向Worker发起任务] taskNo:{}", taskInfo.getTaskNo());
        // 准备参数
        YouJiWorkerReceiveTaskParam receiveTaskParam = new YouJiWorkerReceiveTaskParam();
        BeanUtils.copyProperties(taskInfo, receiveTaskParam);
        log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{}", taskInfo.getTaskNo(), taskInfo.getExecType());
        // 找到目标客户端端
        if (YouJiConstant.ExecType.SINGLE_NODE.getCode().equals(taskInfo.getExecType())) {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{} 进入单节点执行发布流程", taskInfo.getTaskNo(), taskInfo.getExecType());
            // todo 获取到最优的工作节点 (算法待优化 先拿执行次数最少的节点)
            TaskWorker taskWorker = youJiTaskService.getSingleNode(taskInfo);
            // 向该节点发送执行请求
            try {
                httpClientUtil.postJSONParameters("http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/rooster/task/worker/receive", JSON.toJSONString(receiveTaskParam));
            } catch (IOException e) {
                log.info("[酉鸡 Manage向Worker 发起任务]  taskNo:{} Worker节点：{}:{} 失败:{}", taskInfo.getTaskNo(), taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
            }
        } else if (YouJiConstant.ExecType.ALL_NODE.getCode().equals(taskInfo.getExecType())) {

        } else {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型不合法:{}", taskInfo.getTaskNo(), taskInfo.getExecType());
        }
    }
}
