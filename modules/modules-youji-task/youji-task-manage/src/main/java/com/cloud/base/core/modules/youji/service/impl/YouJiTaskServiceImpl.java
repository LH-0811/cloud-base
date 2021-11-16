package com.cloud.base.core.modules.youji.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.youji.code.param.YouJiTaskCreateParam;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskInfoDao;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskWorkerDao;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.util.YouJiOkHttpClientUtil;
import com.cloud.base.core.modules.youji.properties.YouJiServerProperties;
import com.cloud.base.core.modules.youji.service.YouJiTaskService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Slf4j
@Service
public class YouJiTaskServiceImpl implements YouJiTaskService {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskWorkerDao taskWorkerDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private YouJiServerProperties properties;

    @Autowired
    private YouJiOkHttpClientUtil httpClientUtil;

    /**
     * 注册定时任务
     *
     * @param param
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerWorker(@Valid YouJiTaskCreateParam param, String workerHost, Integer workerPort) throws Exception {
        log.info("[Rooster-Manage 定时任务worker 注册到manage]RoosterTaskServiceImpl.registerWorker： param={}", JSON.toJSONString(param));
        // 移除无效工作节点
        this.removeDieWorkerNode();
        try {
            for (YouJiTaskCreateParam.RoosterTaskForm roosterTaskForm : param.getParamList()) {
                // 检查taskNo 是否已经存在在数据库中 如果有则不再初始化。
                QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
                taskInfoQueryWrapper.lambda().eq(TaskInfo::getTaskNo, roosterTaskForm.getTaskNo());
                TaskInfo taskInfo = taskInfoDao.getOne(taskInfoQueryWrapper);
                if (taskInfo == null) {
                    // 如果没有则初始化任务信息到数据库中。
                    taskInfo = new TaskInfo();
                    taskInfo.setId(idWorker.nextId());
                    taskInfo.setTaskType(roosterTaskForm.getTaskType());
                    taskInfo.setExecType(roosterTaskForm.getExecType());
                    taskInfo.setTaskNo(roosterTaskForm.getTaskNo());
                    taskInfo.setTaskName(roosterTaskForm.getTaskName());
                    taskInfo.setCorn(roosterTaskForm.getCorn());
                    taskInfo.setTaskUrl(roosterTaskForm.getTaskUrl());
                    taskInfo.setTaskBeanName(roosterTaskForm.getTaskBeanName());
                    taskInfo.setTaskMethod(roosterTaskForm.getTaskMethod());
                    taskInfo.setTaskParam(roosterTaskForm.getTaskParam());
                    taskInfo.setContactsName(roosterTaskForm.getContactsName());
                    taskInfo.setContactsPhone(roosterTaskForm.getContactsPhone());
                    taskInfo.setContactsEmail(roosterTaskForm.getContactsEmail());
                    taskInfo.setEnableFlag(roosterTaskForm.getEnableFlag());
                    taskInfo.setCreateTime(new Date());
                    taskInfo.setUpdateTime(new Date());
                    taskInfo.setLastExecTime(null);
                    taskInfoDao.save(taskInfo);
                }
                // 根据worker的ip port 查看worker表中是否已经有改工作节点。
                QueryWrapper<TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
                taskWorkerQueryWrapper.lambda()
                        .eq(TaskWorker::getTaskId, taskInfo.getId())
                        .eq(TaskWorker::getWorkerIp, workerHost)
                        .eq(TaskWorker::getWorkerPort, workerPort);
                TaskWorker taskWorker = taskWorkerDao.getOne(taskWorkerQueryWrapper);
                if (taskWorker == null) {
                    taskWorker = new TaskWorker();
                    taskWorker.setId(idWorker.nextId());
                    taskWorker.setTaskId(taskInfo.getId());
                    taskWorker.setTaskNo(taskInfo.getTaskNo());
                    taskWorker.setWorkerAppName("");
                    taskWorker.setWorkerIp(workerHost);
                    taskWorker.setWorkerPort(workerPort);
                    taskWorker.setEnableFlag(true);
                    taskWorker.setOnlineFlag(true);
                    taskWorker.setLastHeartBeatTime(new Date());
                    taskWorkerDao.save(taskWorker);
                }
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("Rooster-Task:Worker注册到Manage失败。"));
        }
    }


    @Override
    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void heartBeatCheckWorker() {
        log.info("[Rooster-Manage Worker心跳检测] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 移除无效工作节点
        this.removeDieWorkerNode();
        // 获取到全部的注册节点
        List<TaskWorker> workerList = taskWorkerDao.list();

        for (TaskWorker taskWorker : workerList) {
            // 向该节点发送心跳请求，成功更新LastHeartBeatTime 失败不做修改
            String reqUrl = "http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/rooster/task/worker/heart_beat";
            try {
                Response response = httpClientUtil.syncGet(reqUrl);
                if (response.isSuccessful()) {
                    try {
                        ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
                        if (serverResponse.isSuccess()) {
                            // 心跳响应成功，更新节点心跳时间
                            TaskWorker updateHeartBeat = new TaskWorker();
                            updateHeartBeat.setId(taskWorker.getId());
                            updateHeartBeat.setLastHeartBeatTime(new Date());
                            taskWorkerDao.updateById(updateHeartBeat);
                        } else {
                            log.info("[Rooster-Manage Worker心跳检测] Worker节点返回响应状态 非正常状态: serverResponse.status={}", serverResponse.getStatus());
                        }
                    } catch (Exception e) {
                        log.info("[Rooster-Manage Worker心跳检测] Worker节点返回响应格式错误: response={}", response.body().string());
                    }
                } else {
                    log.info("[Rooster-Manage Worker心跳检测] Worker节点 {}:{} HttpRespCode:{}", taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), response.code());
                }
            } catch (Exception e) {
                log.info("[Rooster-Manage Worker心跳检测] Worker节点 {}:{} 心跳请求失败:{}", taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
            }

        }
    }


    /**
     * 移除无效的工作节点
     */
    private void removeDieWorkerNode() {
        QueryWrapper<TaskWorker> taskWorkerDeleteQueryWrapper = new QueryWrapper<>();

        // 比 当前是时间 减去 最大未反馈时间还要早 则就是废弃的工作节点 直接删除掉
        taskWorkerDeleteQueryWrapper.lambda()
                .le(TaskWorker::getLastHeartBeatTime, DateUtils.addSeconds(new Date(), (0 - properties.getDieNoHeartBeatTime())));
        taskWorkerDao.remove(taskWorkerDeleteQueryWrapper);
    }
}

