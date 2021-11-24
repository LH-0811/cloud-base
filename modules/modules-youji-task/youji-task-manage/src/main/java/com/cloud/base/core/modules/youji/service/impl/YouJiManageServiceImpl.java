package com.cloud.base.core.modules.youji.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.youji.code.constant.YouJiConstant;
import com.cloud.base.core.modules.youji.code.dto.GetWorkerDto;
import com.cloud.base.core.modules.youji.code.exception.YouJiException;
import com.cloud.base.core.modules.youji.code.param.*;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskInfoDao;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskWorkerDao;
import com.cloud.base.core.modules.youji.code.repository.dao.YoujiTaskExecLogDao;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.repository.entity.YoujiTaskExecLog;
import com.cloud.base.core.modules.youji.code.util.YouJiOkHttpClientUtil;
import com.cloud.base.core.modules.youji.properties.YouJiServerProperties;
import com.cloud.base.core.modules.youji.scheduler.SendTaskToWorkerComponent;
import com.cloud.base.core.modules.youji.scheduler.YouJiSchedulerGetWorkerComponent;
import com.cloud.base.core.modules.youji.scheduler.entity.YouJiSchedulerEntity;
import com.cloud.base.core.modules.youji.scheduler.YouJiSchedulerTaskScannerInit;
import com.cloud.base.core.modules.youji.service.YouJiExceptionService;
import com.cloud.base.core.modules.youji.service.YouJiManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Slf4j
@Service
public class YouJiManageServiceImpl implements YouJiManageService {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private YoujiTaskExecLogDao taskExecLogDao;

    @Autowired
    private TaskWorkerDao taskWorkerDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private YouJiServerProperties properties;

    @Autowired
    private YouJiOkHttpClientUtil httpClientUtil;

    @Autowired
    private YouJiSchedulerTaskScannerInit youJiSchedulerTaskScannerInit;

    @Autowired
    private YouJiExceptionService youJiExceptionService;

    @Autowired
    private YouJiSchedulerGetWorkerComponent getWorkerComponent;

    @Autowired
    private YoujiTaskExecLogDao youjiTaskExecLogDao;

    //////////////// 定时任务流程处理

    /**
     * 注册定时任务
     *
     * @param param
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerWorker(@Valid YouJiWorkerRegisterTaskParam param) throws Exception {
        log.info("[YouJi-Manage 定时任务worker 注册到manage]YouJiTaskServiceImpl.registerWorker： param={}", JSON.toJSONString(param));
        // 移除无效工作节点
        this.removeDieWorkerNode();
        try {
            for (YouJiWorkerRegisterTaskParam.YouJiTaskForm youJiTaskForm : param.getParamList()) {
                // 检查taskNo 是否已经存在在数据库中 如果有则不再初始化。
                QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
                taskInfoQueryWrapper.lambda().eq(TaskInfo::getTaskNo, youJiTaskForm.getTaskNo());
                TaskInfo taskInfo = taskInfoDao.getOne(taskInfoQueryWrapper);
                if (taskInfo == null) {
                    // 如果没有则初始化任务信息到数据库中。
                    taskInfo = new TaskInfo();
                    taskInfo.setId(idWorker.nextId());
                    taskInfo.setTaskType(youJiTaskForm.getTaskType());
                    taskInfo.setExecType(youJiTaskForm.getExecType());
                    taskInfo.setTaskNo(youJiTaskForm.getTaskNo());
                    taskInfo.setTaskName(youJiTaskForm.getTaskName());
                    taskInfo.setCorn(youJiTaskForm.getCorn());
                    taskInfo.setTaskUrl(youJiTaskForm.getTaskUrl());
                    taskInfo.setTaskBeanName(youJiTaskForm.getTaskBeanName());
                    taskInfo.setTaskMethod(youJiTaskForm.getTaskMethod());
                    taskInfo.setTaskParam(youJiTaskForm.getTaskParam());
                    taskInfo.setContactsName(youJiTaskForm.getContactsName());
                    taskInfo.setContactsPhone(youJiTaskForm.getContactsPhone());
                    taskInfo.setContactsEmail(youJiTaskForm.getContactsEmail());
                    taskInfo.setEnableFlag(youJiTaskForm.getEnableFlag());
                    taskInfo.setCreateTime(new Date());
                    taskInfo.setUpdateTime(new Date());
                    taskInfo.setLastExecTime(null);
                    taskInfoDao.save(taskInfo);
                }else {
                    // 当数据库中已经存在该任务时，则更新任务信息。
//                    taskInfo.setId(idWorker.nextId());
                    taskInfo.setTaskType(youJiTaskForm.getTaskType());
                    taskInfo.setExecType(youJiTaskForm.getExecType());
                    taskInfo.setTaskNo(youJiTaskForm.getTaskNo());
                    taskInfo.setTaskName(youJiTaskForm.getTaskName());
                    taskInfo.setCorn(youJiTaskForm.getCorn());
                    taskInfo.setTaskUrl(youJiTaskForm.getTaskUrl());
                    taskInfo.setTaskBeanName(youJiTaskForm.getTaskBeanName());
                    taskInfo.setTaskMethod(youJiTaskForm.getTaskMethod());
                    taskInfo.setTaskParam(youJiTaskForm.getTaskParam());
                    taskInfo.setContactsName(youJiTaskForm.getContactsName());
                    taskInfo.setContactsPhone(youJiTaskForm.getContactsPhone());
                    taskInfo.setContactsEmail(youJiTaskForm.getContactsEmail());
                    taskInfo.setEnableFlag(youJiTaskForm.getEnableFlag());
                    taskInfo.setCreateTime(new Date());
                    taskInfo.setUpdateTime(new Date());
//                    taskInfo.setLastExecTime(null);
                    taskInfoDao.updateById(taskInfo);
                }
                // 注册后立即启动定时任务
                youJiSchedulerTaskScannerInit.saveTask(taskInfo);

                // 根据worker的ip port 查看worker表中是否已经有改工作节点。
                QueryWrapper<TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
                taskWorkerQueryWrapper.lambda()
                        .eq(TaskWorker::getTaskId, taskInfo.getId())
                        .eq(TaskWorker::getWorkerIp, param.getWorkIP())
                        .eq(TaskWorker::getWorkerPort, param.getWorkPort());
                TaskWorker taskWorker = taskWorkerDao.getOne(taskWorkerQueryWrapper);
                if (taskWorker == null) {
                    taskWorker = new TaskWorker();
                    taskWorker.setId(idWorker.nextId());
                    taskWorker.setTaskId(taskInfo.getId());
                    taskWorker.setTaskNo(taskInfo.getTaskNo());
                    taskWorker.setWorkerAppName("");
                    taskWorker.setWorkerIp(param.getWorkIP());
                    taskWorker.setWorkerPort(param.getWorkPort());
                    taskWorker.setEnableFlag(true);
                    taskWorker.setOnlineFlag(true);
                    taskWorker.setLastHeartBeatTime(new Date());
                    taskWorkerDao.save(taskWorker);
                }
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("YouJi-Task:Worker注册到Manage失败。"));
        }
    }

    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void heartBeatCheckWorker() {
        log.debug("[YouJi-Manage Worker心跳检测] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // 移除无效工作节点
        this.removeDieWorkerNode();
        // 获取到全部的注册节点
        List<TaskWorker> workerList = taskWorkerDao.list();
        for (TaskWorker taskWorker : workerList) {
            // 向该节点发送心跳请求，成功更新LastHeartBeatTime 失败不做修改
            String reqUrl = "http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/heart_beat";
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
                            updateHeartBeat.setOnlineFlag(Boolean.TRUE);
                            if (taskWorker.getBeatFailNum() > 0) {
                                // 成功后失败 次数 -1
                                updateHeartBeat.setBeatFailNum(taskWorker.getBeatFailNum() - 1);
                            }
                            taskWorkerDao.updateById(updateHeartBeat);
                            continue;
                        } else {
                            log.info("[YouJi-Manage Worker心跳检测] Worker节点返回响应状态 非正常状态: serverResponse.status={}", serverResponse.getStatus());
                        }
                    } catch (Exception e) {
                        log.info("[YouJi-Manage Worker心跳检测] Worker节点返回响应格式错误: response={}", response.body().string());
                    }
                } else {
                    log.info("[YouJi-Manage Worker心跳检测] Worker节点 {}:{} HttpRespCode:{}", taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), response.code());
                }
            } catch (Exception e) {
                log.info("[YouJi-Manage Worker心跳检测] Worker节点 {}:{} 心跳请求失败:{}", taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
            }
            // 如果心跳失败 则 失败次数+1
            TaskWorker updateHeartBeat = new TaskWorker();
            updateHeartBeat.setId(taskWorker.getId());
            updateHeartBeat.setOnlineFlag(Boolean.FALSE);
            if (taskWorker.getBeatFailNum() == null) {
                updateHeartBeat.setBeatFailNum(1);
            } else {
                updateHeartBeat.setBeatFailNum(taskWorker.getBeatFailNum() + 1);
            }
            taskWorkerDao.updateById(updateHeartBeat);
        }
    }

    /**
     * 获取到全部（可用）定时任务
     *
     * @return
     */
    @Override
    public List<TaskInfo> getAllEnableTaskInfo() {
        log.info("[YouJi-Manage 获取到全部定时任务] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda()
                .eq(TaskInfo::getEnableFlag, Boolean.TRUE);
        // 获取到全部的可用任务列表
        return taskInfoDao.list(taskInfoQueryWrapper);
    }


    /**
     * 移除无效的工作节点
     */
    private void removeDieWorkerNode() {
        QueryWrapper<TaskWorker> taskWorkerDeleteQueryWrapper = new QueryWrapper<>();
        // 去掉10次 和10次以上的 心跳无响应的客户端
        taskWorkerDeleteQueryWrapper.lambda()
                .ge(TaskWorker::getBeatFailNum, properties.getDieNoHeartNum());
        taskWorkerDao.remove(taskWorkerDeleteQueryWrapper);
    }

    /**
     * 执行定时任务
     *
     * @param taskNo
     * @throws YouJiException
     */
    @Override
    public void executeTask(String taskNo) throws YouJiException {
        log.info("[酉鸡 立即执行任务] taskNo={}", taskNo);
        // 获取工作节点
        GetWorkerDto getWorkerDto = getWorkerComponent.getTaskWorkerByTaskNo(taskNo);
        // 准备参数
        YouJiWorkerReceiveTaskParam receiveTaskParam = new YouJiWorkerReceiveTaskParam();
        BeanUtils.copyProperties(getWorkerDto.getTaskInfo(), receiveTaskParam);
        log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{}", getWorkerDto.getTaskInfo().getTaskNo(), getWorkerDto.getTaskInfo().getExecType());
        for (TaskWorker taskWorker : getWorkerDto.getTaskWorkerList()) {
            try {
                receiveTaskParam.setWorkerId(taskWorker.getId());
                Response response = httpClientUtil.postJSONParameters("http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/receive", JSON.toJSONString(receiveTaskParam));
                finishTask(receiveTaskParam, getWorkerDto.getTaskInfo(), taskWorker, response);
            } catch (IOException e) {
                log.info("[酉鸡 Manage向Worker 发起任务]  taskNo:{} Worker节点：{}:{} 失败:{}", getWorkerDto.getTaskInfo().getTaskNo(), taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
                throw new YouJiException(YouJiConstant.YouJiErrorEnum.FAIL_SEND_TASK_TO_WORKER, getWorkerDto.getTaskInfo(), taskWorker, e.getMessage());
            } catch (YouJiException e) {
                throw e;
            }
        }
    }

    // 任务执行完成后 这里只处理工作节点成功返回响应的情况
    private void finishTask(YouJiWorkerReceiveTaskParam param, TaskInfo taskInfo, TaskWorker taskWorker, Response response) throws YouJiException, IOException {
        ServerResponse serverResponse = null;
        if (response.code() == 200) { // http 请求响应200
            try {
                serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
            } catch (Exception e) {
                throw new YouJiException(YouJiConstant.YouJiErrorEnum.WORKER_TASK_RESP_ERR, response.body().string());
            }
        }
        // 1. 记录任务执行日志
        YoujiTaskExecLog execLog = new YoujiTaskExecLog();
        execLog.setId(idWorker.nextId());
        execLog.setTaskNo(param.getTaskNo());
        execLog.setTaskName(param.getTaskName());
        execLog.setWorkerId(param.getWorkerId());
        execLog.setWorkerIp(taskWorker.getWorkerIp());
        execLog.setWorkerPort(taskWorker.getWorkerPort());
        execLog.setTaskParam(param.getTaskParam());
        execLog.setContactsName(param.getContactsName());
        execLog.setContactsPhone(param.getContactsPhone());
        execLog.setContactsEmail(param.getContactsEmail());
        execLog.setFinishFlag(serverResponse.isSuccess());
        execLog.setResultMsg(serverResponse.getMsg());
        execLog.setCreateTime(new Date());
        execLog.setUpdateTime(new Date());
        youjiTaskExecLogDao.save(execLog);
        // 2. 修改worker信息
        TaskWorker workerUpdateInfo = new TaskWorker();
        workerUpdateInfo.setId(taskWorker.getId());
        workerUpdateInfo.setExecTaskNum(taskWorker.getExecTaskNum() == null ? 1 : taskWorker.getExecTaskNum() + 1);
        workerUpdateInfo.setLastExecTime(new Date());
        taskWorkerDao.updateById(workerUpdateInfo);
        // 3. 修改任务信息
        TaskInfo updateTask = new TaskInfo();
        updateTask.setId(taskInfo.getId());
        updateTask.setExecNum(taskInfo.getExecNum() == null ? 1 : taskInfo.getExecNum() + 1);
        updateTask.setLastExecTime(new Date());
        taskInfoDao.updateById(updateTask);
    }
    //////////////// 信息管理

    /**
     * 查询定时任务列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<TaskInfo> queryTask(YouJiTaskInfoQueryParam param) throws Exception {
        log.info("[酉鸡 查询定时任务列表] param={}", JSON.toJSONString(param));
        try {
            QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<TaskInfo> queryLambda = taskInfoQueryWrapper.lambda();
            if (StringUtils.isNotBlank(param.getTaskType())) {
                queryLambda.eq(TaskInfo::getTaskType, param.getTaskType());
            }
            if (StringUtils.isNotBlank(param.getExecType())) {
                queryLambda.eq(TaskInfo::getExecType, param.getExecType());
            }
            if (StringUtils.isNotBlank(param.getTaskNo())) {
                queryLambda.like(TaskInfo::getTaskNo, "%" + param.getTaskNo() + "%");
            }
            if (StringUtils.isNotBlank(param.getTaskName())) {
                queryLambda.like(TaskInfo::getTaskName, "%" + param.getTaskName() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsName())) {
                queryLambda.like(TaskInfo::getContactsName, "%" + param.getContactsName() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsPhone())) {
                queryLambda.like(TaskInfo::getContactsPhone, "%" + param.getContactsPhone() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsEmail())) {
                queryLambda.like(TaskInfo::getContactsEmail, "%" + param.getContactsEmail() + "%");
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<TaskInfo> list = taskInfoDao.list(queryLambda);
            PageInfo<TaskInfo> pageInfo = new PageInfo<>(list);
            PageHelper.clearPage();
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取定时任务列表失败"));
        }
    }


    /**
     * @param param
     * @throws Exception
     */
    @Override
    public void updateTask(YouJiTaskInfoBaseInfoUpdateParam param) throws Exception {
        log.info("[酉鸡 更新定时任务基本信息] param={}", JSON.toJSONString(param));
        try {
            TaskInfo updateTaskInfo = new TaskInfo();
            BeanUtils.copyProperties(param, updateTaskInfo);
            // 更新定时任务基础数据
            taskInfoDao.updateById(updateTaskInfo);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("更新基础数据失败"));
        }
    }


    /**
     * 更新定时任务执行计划
     *
     * @param param
     * @throws Exception
     */
    @Override
    public void changeCron(YouJiTaskInfoCronUpdateParam param) throws Exception {
        log.info("[酉鸡 更新定时任务执行计划] param={}", JSON.toJSONString(param));
        TaskInfo taskInfo = taskInfoDao.getById(param.getId());
        if (taskInfo == null) {
            throw CommonException.create(ServerResponse.createByError("定时任务不存在"));
        }

        // 修改定时任务执行策略
        TaskInfo updateInfo = new TaskInfo();
        updateInfo.setId(taskInfo.getId());
        updateInfo.setCorn(param.getCron());
        updateInfo.setUpdateTime(new Date());
        taskInfoDao.updateById(updateInfo);
        taskInfo.setCorn(param.getCron());
        // 保存定时任务修改
        youJiSchedulerTaskScannerInit.saveTask(taskInfo);
//        // 更新定时任务执行计划
//        HashMap<String, YouJiSchedulerEntity> schedulerEntityHashMap = youJiSchedulerTaskScannerInit.getSchedulerEntityHashMap();
//        // 获取原任务的执行计划
//        YouJiSchedulerEntity schedulerEntity = schedulerEntityHashMap.get(taskInfo.getTaskNo());
//        // 如果该任务是可用状态 需要直接停用启动
//        if (taskInfo.getEnableFlag()) {
//            taskInfo.setCorn(param.getCron());
//            if (schedulerEntity == null) {
//                schedulerEntity = new YouJiSchedulerEntity();
//            } else {
//                if (!schedulerEntity.getFuture().isCancelled()) {
//                    // 取消定时任务 如果执行中是否中断
//                    schedulerEntity.getFuture().cancel(true);
//                }
//            }
//            // 覆盖原定时任务执行计划
//            schedulerEntity.setTaskNo(taskInfo.getTaskNo());
//            schedulerEntity.setTaskInfo(taskInfo);
//            ScheduledFuture<?> schedule = youJiSchedulerTaskScannerInit.getThreadPoolTaskScheduler().schedule(new SendTaskToWorkerComponent(taskInfo.getTaskNo(), this, youJiExceptionService), new CronTrigger(taskInfo.getCorn()));
//            schedulerEntity.setFuture(schedule);
//            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
//        } else {
//            // 如果是停用状态
//            schedulerEntityHashMap.remove(taskInfo.getTaskNo());
//        }
    }


    /**
     * 修改定时任务停止 和 启动
     *
     * @param param
     * @throws Exception
     */
    @Override
    public void changeTaskEnable(YouJiTaskInfoEnableUpdateParam param) throws Exception {
        log.info("[酉鸡 更新定时任务是否可用] param={}", JSON.toJSONString(param));
        TaskInfo taskInfo = taskInfoDao.getById(param.getId());
        if (taskInfo == null) {
            throw CommonException.create(ServerResponse.createByError("定时任务不存在"));
        }

        // 修改定时任务执行策略
        TaskInfo updateInfo = new TaskInfo();
        updateInfo.setId(taskInfo.getId());
        updateInfo.setEnableFlag(param.getEnableFlag());
        updateInfo.setUpdateTime(new Date());
        taskInfoDao.updateById(updateInfo);

        taskInfo.setEnableFlag(param.getEnableFlag());
        // 保存定时任务修改
        youJiSchedulerTaskScannerInit.saveTask(taskInfo);

//        // 更新定时任务执行计划
//        HashMap<String, YouJiSchedulerEntity> schedulerEntityHashMap = youJiSchedulerTaskScannerInit.getSchedulerEntityHashMap();
//        // 获取原任务的执行计划
//        YouJiSchedulerEntity schedulerEntity = schedulerEntityHashMap.get(taskInfo.getTaskNo());

//        if (param.getEnableFlag()) {
//            // 启动定时任务
//            if (schedulerEntity == null) {
//                schedulerEntity = new YouJiSchedulerEntity();
//            } else {
//                if (!schedulerEntity.getFuture().isCancelled()) {
//                    // 取消定时任务 如果执行中是否中断
//                    schedulerEntity.getFuture().cancel(true);
//                }
//            }
//            // 覆盖原定时任务执行计划
//            schedulerEntity.setTaskNo(taskInfo.getTaskNo());
//            schedulerEntity.setTaskInfo(taskInfo);
//            ScheduledFuture<?> schedule = youJiSchedulerTaskScannerInit.getThreadPoolTaskScheduler().schedule(new SendTaskToWorkerComponent(taskInfo.getTaskNo(), this, youJiExceptionService), new CronTrigger(taskInfo.getCorn()));
//            schedulerEntity.setFuture(schedule);
//            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
//        } else {
//            // 停止定时任务
//            if (schedulerEntity != null) {
//                if (!schedulerEntity.getFuture().isCancelled()) {
//                    // 取消定时任务 如果执行中是否中断
//                    schedulerEntity.getFuture().cancel(true);
//                }
//            }
//            schedulerEntityHashMap.remove(taskInfo.getTaskNo());
//        }

    }


    /**
     * 查询定时任务日志列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<YoujiTaskExecLog> queryTaskLog(YouJiTaskInfoLogQueryParam param) throws Exception {
        log.info("[酉鸡 查询定时任务日志列表] param={}", JSON.toJSONString(param));
        try {
            QueryWrapper<YoujiTaskExecLog> taskInfoQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<YoujiTaskExecLog> queryLambda = taskInfoQueryWrapper.lambda();
            queryLambda.orderByDesc(YoujiTaskExecLog::getCreateTime);

            if (StringUtils.isNotBlank(param.getTaskNo())) {
                queryLambda.like(YoujiTaskExecLog::getTaskNo, "%" + param.getTaskNo() + "%");
            }
            if (StringUtils.isNotBlank(param.getTaskName())) {
                queryLambda.like(YoujiTaskExecLog::getTaskName, "%" + param.getTaskName() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsName())) {
                queryLambda.like(YoujiTaskExecLog::getContactsName, "%" + param.getContactsName() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsPhone())) {
                queryLambda.like(YoujiTaskExecLog::getContactsPhone, "%" + param.getContactsPhone() + "%");
            }
            if (StringUtils.isNotBlank(param.getContactsEmail())) {
                queryLambda.like(YoujiTaskExecLog::getContactsEmail, "%" + param.getContactsEmail() + "%");
            }
            if (param.getFinishFlag() != null) {
                queryLambda.eq(YoujiTaskExecLog::getFinishFlag, param.getFinishFlag());
            }
            if (param.getExecTimeLow() != null) {
                queryLambda.ge(YoujiTaskExecLog::getCreateTime, param.getExecTimeLow());
            }
            if (param.getExecTimeLow() != null) {
                queryLambda.le(YoujiTaskExecLog::getCreateTime, param.getExecTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<YoujiTaskExecLog> list = taskExecLogDao.list(queryLambda);
            PageInfo<YoujiTaskExecLog> pageInfo = new PageInfo<>(list);
            PageHelper.clearPage();
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取定时任务列表失败"));
        }
    }


    /**
     * 获取定时任务的工作节点列表
     *
     * @param taskNo
     * @return
     * @throws Exception
     */
    @Override
    public List<TaskWorker> getWorkListByTaskNo(String taskNo) throws Exception {
        log.info("[酉鸡 获取工作节点列表] taskNo={}", taskNo);
        QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda().eq(TaskInfo::getTaskNo, taskNo);
        TaskInfo taskInfo = taskInfoDao.getOne(taskInfoQueryWrapper);
        if (taskInfo == null) {
            throw CommonException.create(ServerResponse.createByError("定时任务不存在"));
        }
        try {
            QueryWrapper<TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
            taskWorkerQueryWrapper.lambda().eq(TaskWorker::getTaskId, taskInfo.getId());
            return taskWorkerDao.list(taskWorkerQueryWrapper);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取获取工作节点列表失败"));
        }

    }


    /**
     * 修改worker节点是否可用
     *
     * @param param
     * @throws Exception
     */
    @Override
    public void changeWorkerEnable(YouJiTaskWorkerEnableUpdateParam param) throws Exception {
        log.info("[酉鸡 修改worker节点是否可用] param={}", JSON.toJSONString(param));
        TaskWorker taskWorker = taskWorkerDao.getById(param.getId());
        if (taskWorker == null) {
            throw CommonException.create(ServerResponse.createByError("工作节点不存在"));
        }
        try {
            TaskWorker updateWorker = new TaskWorker();
            updateWorker.setId(param.getId());
            updateWorker.setEnableFlag(param.getEnableFlag());
            taskWorkerDao.updateById(updateWorker);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取获取工作节点列表失败"));
        }
    }
}

