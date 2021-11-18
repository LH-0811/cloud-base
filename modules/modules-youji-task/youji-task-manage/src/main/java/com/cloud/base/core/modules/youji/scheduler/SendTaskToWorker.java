package com.cloud.base.core.modules.youji.scheduler;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.modules.youji.code.constant.YouJiConstant;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.util.YouJiOkHttpClientUtil;
import com.cloud.base.core.modules.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
public class SendTaskToWorker implements Runnable {

    private YouJiManageService youJiManageService;
    private YouJiOkHttpClientUtil httpClientUtil;
    private TaskInfo taskInfo;

    public SendTaskToWorker(TaskInfo taskInfo, YouJiManageService youJiManageService, YouJiOkHttpClientUtil httpClientUtil) {
        this.taskInfo = taskInfo;
        this.httpClientUtil = httpClientUtil;
        this.youJiManageService = youJiManageService;
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
            // todo liuhe 获取到最优的工作节点 (算法待优化 先拿执行次数最少的节点)
            TaskWorker taskWorker = youJiManageService.getSingleNode(taskInfo);
            // 向该节点发送执行请求
            try {
                receiveTaskParam.setWorkerId(taskWorker.getId());
                httpClientUtil.postJSONParameters("http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/receive", JSON.toJSONString(receiveTaskParam));
            } catch (IOException e) {
                log.info("[酉鸡 Manage向Worker 发起任务]  taskNo:{} Worker节点：{}:{} 失败:{}", taskInfo.getTaskNo(), taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
            }
        } else if (YouJiConstant.ExecType.ALL_NODE.getCode().equals(taskInfo.getExecType())) {
            List<TaskWorker> allNode = youJiManageService.getAllNode(taskInfo);
            for (TaskWorker taskWorker : allNode) {
                try {
                    receiveTaskParam.setWorkerId(taskWorker.getId());
                    httpClientUtil.postJSONParameters("http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/receive", JSON.toJSONString(receiveTaskParam));
                } catch (IOException e) {
                    log.info("[酉鸡 Manage向Worker 发起任务]  taskNo:{} Worker节点：{}:{} 失败:{}", taskInfo.getTaskNo(), taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
                }
            }
        } else {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型不合法:{}", taskInfo.getTaskNo(), taskInfo.getExecType());
        }
    }
}
