package com.cloud.base.common.youji.cronjob.mgr.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.youji.cronjob.core.constant.Youji2Constant;
import com.cloud.base.common.youji.cronjob.core.entity.GetWorkerDto;
import com.cloud.base.common.youji.cronjob.core.exception.Youji2Exception;
import com.cloud.base.common.youji.cronjob.core.param.Youji2WorkerReceiveTaskParam;
import com.cloud.base.common.youji.cronjob.core.param.Youji2TaskRegisterParam;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2TaskExecLogDao;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2TaskInfoDao;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2TaskMgrDao;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2TaskWorkerDao;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskExecLog;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskMgr;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskWorker;
import com.cloud.base.common.youji.cronjob.core.util.YouJi2OkHttpClientUtil;
import com.cloud.base.common.youji.cronjob.mgr.component.Youji2MgrTaskSendComponent;
import com.cloud.base.common.youji.cronjob.mgr.constant.Youji2MgrConstant;
import com.cloud.base.common.youji.cronjob.mgr.entity.Youji2SchedulerEntity;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ConfigService;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ExceptionService;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2022/4/13
 */
@Slf4j
@Service
public class Youji2MgrServiceImpl implements Youji2MgrService {

    @Autowired
    private Youji2TaskInfoDao youji2TaskInfoDao;

    @Autowired
    private Youji2TaskMgrDao youji2TaskMgrDao;

    @Autowired
    private Youji2TaskWorkerDao youji2TaskWorkerDao;

    @Autowired
    private Youji2ServerProperties properties;

    @Autowired
    private Youji2ConfigService youji2ConfigService;

    @Autowired
    private IdWorker idWorker;

    @Value("${server.port:8080}")
    private Integer port;

    @Value("${spring.application.name}")
    private String serverName;

    @Autowired
    private YouJi2OkHttpClientUtil httpClientUtil;

    @Autowired
    private Youji2TaskExecLogDao youji2TaskExecLogDao;

    @Autowired
    private Youji2ExceptionService youji2ExceptionService;


    @Autowired
    private HashMap<String, Youji2SchedulerEntity> schedulerEntityHashMap;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;


    /**
     * 注册任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerTask(Youji2TaskRegisterParam param) throws Exception {
        log.info("[YouJi-Manage 定时任务worker 注册到manage]YouJiTaskServiceImpl.registerWorker： param={}", JSON.toJSONString(param));
        // 移除无效工作节点
        this.removeDieWorkerNode();
        try {
            for (Youji2TaskRegisterParam.YouJiTaskForm youJiTaskForm : param.getParamList()) {
                // 注册的定时任务
                Youji2TaskInfo taskInfo = null;
                if (BooleanUtils.isTrue(param.getIsWorkerRegister())) {
                    // 1.将任务数据保存到数据库中
                    taskInfo = saveTaskInfoToMysql(param, youJiTaskForm);
                }

                // 2.判断任务是否注册完成
                if (properties.getMgrIndex().compareTo(taskInfo.getStartRegisterMgrIndex()) == 0 && BooleanUtils.isFalse(param.getIsWorkerRegister()) && BooleanUtils.isFalse(taskInfo.getRegisterCompleteFlag())) {
                    // 3. 如果当前节点就是客户端开始注册的节点 并且 任务是由其他管理节点推送过来的 说明已经注册完成了
                    Youji2TaskInfo updateInfo = new Youji2TaskInfo();
                    updateInfo.setId(taskInfo.getId());
                    updateInfo.setRegisterCompleteFlag(Boolean.TRUE);
                    youji2TaskInfoDao.updateById(updateInfo);

                    taskInfo.setRegisterCompleteFlag(Boolean.TRUE);
                }

                // 3. 判断任务是否发给下个管理节点 （未注册完成）
                if (BooleanUtils.isFalse(taskInfo.getRegisterCompleteFlag())) {
                    // 获取到当前节点
                    Youji2TaskMgr currentMgr = getTaskMgrByIndex(properties.getMgrIndex());
                    // 获取下个节点
                    Youji2TaskMgr nextMgr = getTaskMgrByIndex(currentMgr.getPingMgrIndex());
                    // 如果下个节点存在 并且是online
                    if (nextMgr != null && BooleanUtils.isTrue(nextMgr.getEnableFlag())) {
                        // 把是否客户端注册请求改为false
                        param.setIsWorkerRegister(Boolean.FALSE);
                        // todo 所有的请求 都要整合到同一个sdk中 增加请求失败后重试的机制。
                        Response response = httpClientUtil.postJSONParameters("http://" + nextMgr.getMgrIp() + ":" + nextMgr.getMgrPort() + "/youji/mgr/task/register", JSON.toJSONString(param));
                        if (response.isSuccessful()) {
                            ServerResponse serverResponse = JSON.parseObject(response.body().toString(), ServerResponse.class);
                            if (serverResponse.isSuccess()) {

                            }
                        }
                    }
                }

                // 4. 开启任务监听
                startTask(taskInfo);
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("YouJi-Task:Worker注册到Manage失败。"));
        }
    }

    // 保存定时任务修改并立即生效启动定时任务
    public void startTask(Youji2TaskInfo taskInfo) {
        // 获取原任务的执行计划
        Youji2SchedulerEntity schedulerEntity = schedulerEntityHashMap.get(taskInfo.getTaskNo());

        // 先中断之前的定时任务
        if (schedulerEntity == null) {
            schedulerEntity = new Youji2SchedulerEntity();
        } else {
            if (!schedulerEntity.getFuture().isCancelled()) {
                // 取消定时任务 如果执行中是否中断
                schedulerEntity.getFuture().cancel(true);
            }
        }

        // 覆盖原定时任务执行计划
        schedulerEntity.setTaskNo(taskInfo.getTaskNo());
        schedulerEntity.setTaskInfo(taskInfo);
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new Youji2MgrTaskSendComponent(taskInfo.getTaskNo(), this, youji2ExceptionService), new CronTrigger(taskInfo.getCorn()));
        schedulerEntity.setFuture(schedule);
        schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
    }

    /**
     * 获取全部可用的任务
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Youji2TaskInfo> getAllTaskInfo() throws Exception {
        QueryWrapper<Youji2TaskInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Youji2TaskInfo::getTaskNo);
        return youji2TaskInfoDao.list(queryWrapper);
    }

    /**
     * 获取全部可用 在线的管理节点
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Youji2TaskMgr> getEnableOnlineTaskMgr() throws Exception {
        QueryWrapper<Youji2TaskMgr> mgrQueryWrapper = new QueryWrapper<>();
        mgrQueryWrapper.lambda().orderByAsc(Youji2TaskMgr::getMgrIndex).eq(Youji2TaskMgr::getEnableFlag, Boolean.TRUE);
        return youji2TaskMgrDao.list(mgrQueryWrapper);
    }


    /**
     * 根据管理节点的index获取管理节点
     *
     * @param mgrIndex
     * @return
     * @throws Exception
     */
    @Override
    public Youji2TaskMgr getTaskMgrByIndex(Integer mgrIndex) throws Exception {
        QueryWrapper<Youji2TaskMgr> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                /*.eq(Youji2TaskMgr::getEnableFlag,Boolean.TRUE)*/
                .eq(Youji2TaskMgr::getMgrIndex, mgrIndex);
        Youji2TaskMgr mgr = youji2TaskMgrDao.getOne(queryWrapper);
        return mgr;
    }


    /**
     * 保存或者更新当前节点的管理节点
     *
     * @throws Exception
     */
    @Override
    public void saveOrUpdateCurrentNodeTaskMgr() throws Exception {
        Youji2TaskMgr mgr = getTaskMgrByIndex(properties.getMgrIndex());
        if (mgr != null) {
            mgr.setMgrIndex(properties.getMgrIndex());
            mgr.setMgrIp(properties.getMgrIp());
            mgr.setMgrPort(port);
            youji2TaskMgrDao.updateById(mgr);
        } else {
            mgr = new Youji2TaskMgr();
            mgr.setId(idWorker.nextId());
            mgr.setMgrIndex(properties.getMgrIndex());
            mgr.setMgrIp(properties.getMgrIp());
            mgr.setMgrPort(port);
            mgr.setEnableFlag(properties.getEnable());
            mgr.setMgrServerName(serverName);
            mgr.setBeatFailNum(0);
            mgr.setLastHeartBeatTime(new Date());
            youji2TaskMgrDao.save(mgr);
        }
    }


    /**
     * 更新主节点的绑定关系
     *
     * @throws Exception
     */
    @Override
    public void updateTaskMgrBindEchoMgrRel() throws Exception {
        // 绑定
        // 获取到全部的节点 按照index升序排序
        List<Youji2TaskMgr> allMgr = getEnableOnlineTaskMgr();

        // 没有节点或者节点数是1 就跳过 没有相互的ping监控保活
        if (CollectionUtils.isEmpty(allMgr) || allMgr.size() == 1) return;

        // 更新echo关系
        for (int i = 0; i < allMgr.size(); i++) {
            // 当前节点
            Youji2TaskMgr currentMgr = allMgr.get(i);
            // 当前节点负责echo观察的节点
            Youji2TaskMgr echoMgr = null;
            if ((i + i) < allMgr.size()) {
                echoMgr = allMgr.get(i + 1);
            } else {
                echoMgr = allMgr.get(0);
            }
            // 绑定
            Youji2TaskMgr updateMgr = new Youji2TaskMgr();
            updateMgr.setId(currentMgr.getId());
            updateMgr.setPingMgrIndex(echoMgr.getMgrIndex());
            youji2TaskMgrDao.updateById(updateMgr);
        }
    }


    /**
     * echo成功后 更新被echo的节点数据
     *
     * @param taskMgr
     * @throws Exception
     */
    @Override
    public void echoSuccess(Youji2TaskMgr taskMgr) throws Exception {
        log.info("[酉鸡2 echo成功] {}:{} ---> {}:{}", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
        // 最后一次echo的时间
        taskMgr.setLastHeartBeatTime(new Date());
        // 心跳失败数
        if (taskMgr.getBeatFailNum() == null) {
            taskMgr.setBeatFailNum(0);
        } else {
            Integer failNum = (taskMgr.getBeatFailNum() - 1) < 0 ? 0 : taskMgr.getBeatFailNum() - 1;
            taskMgr.setBeatFailNum(failNum);
        }

        // 当最终的失败次数累计恢复到0后，重新上线这个主节点
        if (taskMgr.getBeatFailNum() <= 0 && BooleanUtils.isFalse(taskMgr.getEnableFlag())) {
            log.info("[酉鸡2 echo成功] {}:{} ---> {}:{} 恢复主节点可用", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            taskMgr.setEnableFlag(Boolean.TRUE);
            youji2TaskMgrDao.updateById(taskMgr);
            // 重新上线后要重新绑定监听关系
            log.info("[酉鸡2 echo成功] {}:{} ---> {}:{} 重新绑定主节点echo关系", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            updateTaskMgrBindEchoMgrRel();

            // 分发任务给管理节点
            log.info("[酉鸡2 echo成功] {}:{} ---> {}:{} 重新分配任务给主节点", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            dispatchTaskToMgr();
        } else {
            youji2TaskMgrDao.updateById(taskMgr);
        }

    }


    /**
     * echo失败后 更新被echo的节点数据
     *
     * @param taskMgr
     * @throws Exception
     */
    @Override
    public void echoFail(Youji2TaskMgr taskMgr) throws Exception {
        log.info("[酉鸡2 echo失败] {}:{} ---> {}:{}", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
        // 最后一次echo的时间
        taskMgr.setLastHeartBeatTime(new Date());
        // 心跳失败数
        if (taskMgr.getBeatFailNum() == null) {
            taskMgr.setBeatFailNum(1);
        } else {
            Integer failNum = (taskMgr.getBeatFailNum() + 1) > properties.getHeartBeatFailNum() ? properties.getHeartBeatFailNum() : taskMgr.getBeatFailNum() + 1;
            taskMgr.setBeatFailNum(failNum);
        }
        // 当最终的失败次数累计到最上限时 强制下线这个节点
        if (taskMgr.getBeatFailNum() >= properties.getHeartBeatFailNum() && BooleanUtils.isTrue(taskMgr.getEnableFlag())) {
            log.info("[酉鸡2 echo失败] {}:{} ---> {}:{}  主节点达到echo失败次数上限，主动下单主节点", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            taskMgr.setEnableFlag(Boolean.FALSE);
            youji2TaskMgrDao.updateById(taskMgr);
            // 下线后要重新绑定监听关系
            log.info("[酉鸡2 echo失败] {}:{} ---> {}:{}  重新绑定主节点echo关系", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            updateTaskMgrBindEchoMgrRel();

            // 分发任务给管理节点
            log.info("[酉鸡2 echo失败] {}:{} ---> {}:{}  重新分配任务给主节点", properties.getMgrIp(), port, taskMgr.getMgrIp(), taskMgr.getMgrPort());
            dispatchTaskToMgr();
        } else {
            youji2TaskMgrDao.updateById(taskMgr);
        }

    }


    /**
     * 分发任务到管理节点
     *
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void dispatchTaskToMgr() throws Exception {
        // 任务数据
        log.info("[酉鸡 获取全部的任务]");
        List<Youji2TaskInfo> taskInfoList = getAllTaskInfo();
        if (CollectionUtils.isEmpty(taskInfoList)) {
            return;
        }

        log.info("[酉鸡 获取全部的管理节点]");
        List<Youji2TaskMgr> mgrList = getEnableOnlineTaskMgr();
        if (CollectionUtils.isEmpty(mgrList)) {
            return;
        }

        // 触发算法 去分发任务
        Boolean lockForKey = youji2ConfigService.getLockForKey(Youji2MgrConstant.YoujiTaskConfig.SCAN_MGR_TASK_LOCK.getCode());
        if (lockForKey) {
            try {
                // 管理节点不为空 任务不为空时分配任务
                List<Youji2TaskInfo> updateTaskInfoList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(mgrList) && CollectionUtils.isNotEmpty(taskInfoList)) {
                    for (int i = 0; i < taskInfoList.size(); i++) {
                        // 当前任务
                        Youji2TaskInfo currentTask = taskInfoList.get(i);
                        // 更新到管理节点
                        Youji2TaskMgr youji2TaskMgr = mgrList.get(i % mgrList.size());
                        Youji2TaskInfo updateInfo = new Youji2TaskInfo();
                        updateInfo.setId(currentTask.getId());
                        updateInfo.setTaskNo(currentTask.getTaskNo());
                        updateInfo.setBindMgrIndex(youji2TaskMgr.getMgrIndex());
                        updateTaskInfoList.add(updateInfo);
                    }
                }
                // 更新定时任务
                youji2TaskInfoDao.updateBatchById(updateTaskInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                youji2ConfigService.releaseLockForKey(Youji2MgrConstant.YoujiTaskConfig.SCAN_MGR_TASK_LOCK.getCode());
            }
        }

    }


    /**
     * 执行定时任务
     *
     * @param taskNo
     * @throws Youji2Exception
     */
    @Override
    public void executeTask(String taskNo) throws Youji2Exception {
        log.info("[酉鸡2 立即执行任务] taskNo={}", taskNo);
        // 获取工作节点
        GetWorkerDto getWorkerDto = getTaskWorkerByTaskNo(taskNo);
        // 获取到任务信息
        Youji2TaskInfo taskInfo = getWorkerDto.getTaskInfo();
        if (!taskInfo.getEnableFlag()) {
            log.info("[酉鸡2 立即执行任务] 当前任务不可用 taskNo={} enable={}", taskInfo.getTaskNo(), taskInfo.getEnableFlag());
            return;
        }
        if (properties.getMgrIndex().compareTo(taskInfo.getBindMgrIndex()) != 0) {
            log.info("[酉鸡2 立即执行任务] 当前任务非当前节点 taskNo.bindIndex={} currentIndex={}", taskInfo.getBindMgrIndex(), properties.getMgrIndex());
            return;
        }
        // 准备参数
        Youji2WorkerReceiveTaskParam receiveTaskParam = new Youji2WorkerReceiveTaskParam();
        BeanUtils.copyProperties(getWorkerDto.getTaskInfo(), receiveTaskParam);
        log.info("[酉鸡2 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{}", getWorkerDto.getTaskInfo().getTaskNo(), getWorkerDto.getTaskInfo().getExecType());

//        for (Youji2TaskWorker taskWorker : getWorkerDto.getTaskWorkerList()) {
//            try {
//                receiveTaskParam.setWorkerId(taskWorker.getId());
//                Response response = httpClientUtil.postJSONParameters("http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/receive", JSON.toJSONString(receiveTaskParam));
//                finishTask(receiveTaskParam, getWorkerDto.getTaskInfo(), taskWorker, response);
//            } catch (IOException e) {
//                log.info("[酉鸡2 Manage向Worker 发起任务]  taskNo:{} Worker节点：{}:{} 失败:{}", getWorkerDto.getTaskInfo().getTaskNo(), taskWorker.getWorkerIp(), taskWorker.getWorkerPort(), e);
//                throw new Youji2Exception(Youji2Constant.YouJiErrorEnum.FAIL_SEND_TASK_TO_WORKER, getWorkerDto.getTaskInfo(), taskWorker, e.getMessage());
//            } catch (Youji2Exception e) {
//                throw e;
//            }
//        }
    }


    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void echoWorker() {
        log.debug("[YouJi-Manage Worker心跳检测] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // 移除无效工作节点
        this.removeDieWorkerNode();
        // 获取到该节点对应的任务的工作节点
        List<Youji2TaskWorker> workerList = getTaskWorkerListByMgrIndex();
        for (Youji2TaskWorker taskWorker : workerList) {
            // 向该节点发送心跳请求，成功更新LastHeartBeatTime 失败不做修改
            String reqUrl = "http://" + taskWorker.getWorkerIp() + ":" + taskWorker.getWorkerPort() + "/youji/task/worker/heart_beat";
            try {
                Response response = httpClientUtil.syncGet(reqUrl);
                if (response.isSuccessful()) {
                    try {
                        ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
                        if (serverResponse.isSuccess()) {
                            // 心跳响应成功，更新节点心跳时间
                            Youji2TaskWorker updateHeartBeat = new Youji2TaskWorker();
                            updateHeartBeat.setId(taskWorker.getId());
                            updateHeartBeat.setLastHeartBeatTime(new Date());
                            updateHeartBeat.setOnlineFlag(Boolean.TRUE);
                            if (taskWorker.getBeatFailNum() > 0) {
                                // 成功后失败 次数 -1
                                updateHeartBeat.setBeatFailNum(taskWorker.getBeatFailNum() - 1);
                            }
                            youji2TaskWorkerDao.updateById(updateHeartBeat);
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
            Youji2TaskWorker updateHeartBeat = new Youji2TaskWorker();
            updateHeartBeat.setId(taskWorker.getId());
            updateHeartBeat.setOnlineFlag(Boolean.FALSE);
            if (taskWorker.getBeatFailNum() == null) {
                updateHeartBeat.setBeatFailNum(1);
            } else {
                updateHeartBeat.setBeatFailNum(taskWorker.getBeatFailNum() + 1);
            }
            youji2TaskWorkerDao.updateById(updateHeartBeat);
        }
    }


    /**
     * 获取任务信息
     *
     * @param taskNo
     * @return
     * @throws Youji2Exception
     */
    private GetWorkerDto getTaskWorkerByTaskNo(String taskNo) throws Youji2Exception {
        GetWorkerDto getWorkerDto = new GetWorkerDto();
        QueryWrapper<Youji2TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda().eq(Youji2TaskInfo::getTaskNo, taskNo);
        Youji2TaskInfo taskInfo = youji2TaskInfoDao.getOne(taskInfoQueryWrapper);
        if (taskInfo == null) {
            throw new Youji2Exception(Youji2Constant.YouJiErrorEnum.NOT_EXIST_TASK, taskNo);
        }
        getWorkerDto.setTaskInfo(taskInfo);
        // 找到目标客户端端
        if (Youji2Constant.ExecType.SINGLE_NODE.getCode().equals(taskInfo.getExecType())) {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{} 进入单节点执行发布流程", taskInfo.getTaskNo(), taskInfo.getExecType());
            // todo liuhe 获取到最优的工作节点 (算法待优化 先拿执行次数最少的节点)
            Youji2TaskWorker taskWorker = getSingleNode(taskInfo);
            getWorkerDto.setTaskWorkerList(Lists.newArrayList(taskWorker));
            return getWorkerDto;
        } /*else if (Youji2Constant.ExecType.ALL_NODE.getCode().equals(taskInfo.getExecType())) {
            List<Youji2TaskWorker> allNode = getAllNode(taskInfo);
            getWorkerDto.setTaskWorkerList(allNode);
            return getWorkerDto;
        } */ else {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型不合法:{}", taskInfo.getTaskNo(), taskInfo.getExecType());
            throw new Youji2Exception(Youji2Constant.YouJiErrorEnum.TASK_EXEC_TYPE_ERR, taskInfo, "ExecType=" + taskInfo.getExecType());
        }
    }

    /**
     * 获取一个工作节点
     * @param taskInfo
     * @return
     * @throws Youji2Exception
     */
    private Youji2TaskWorker getSingleNode(Youji2TaskInfo taskInfo) throws Youji2Exception {
        log.info("[YouJi-Manage 挑选一个工作节点] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<Youji2TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
        taskWorkerQueryWrapper.lambda()
                .eq(Youji2TaskWorker::getTaskId, taskInfo.getId())
                .eq(Youji2TaskWorker::getTaskNo, taskInfo.getTaskNo())
                .eq(Youji2TaskWorker::getBeatFailNum, 0)
                .eq(Youji2TaskWorker::getEnableFlag, Boolean.TRUE)
                .eq(Youji2TaskWorker::getOnlineFlag, Boolean.TRUE)
                .orderByAsc(Youji2TaskWorker::getExecTaskNum);
        List<Youji2TaskWorker> taskWorkerList = youji2TaskWorkerDao.list(taskWorkerQueryWrapper);
        if (CollectionUtils.isEmpty(taskWorkerList)) {
            throw new Youji2Exception(Youji2Constant.YouJiErrorEnum.NOT_FIND_WORKER, taskInfo, "");
        }
        return taskWorkerList.get(0);
    }

    // 任务执行完成后 这里只处理工作节点成功返回响应的情况
    private void finishTask(Youji2WorkerReceiveTaskParam param, Youji2TaskInfo taskInfo, Youji2TaskWorker taskWorker, Response response) throws Youji2Exception, IOException {
        ServerResponse serverResponse = null;
        if (response.code() == 200) { // http 请求响应200
            try {
                serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
            } catch (Exception e) {
                throw new Youji2Exception(Youji2Constant.YouJiErrorEnum.WORKER_TASK_RESP_ERR, response.body().string());
            }
        }
        // 1. 记录任务执行日志
        Youji2TaskExecLog execLog = new Youji2TaskExecLog();
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
        youji2TaskExecLogDao.save(execLog);
        // 2. 修改worker信息
        Youji2TaskWorker workerUpdateInfo = new Youji2TaskWorker();
        workerUpdateInfo.setId(taskWorker.getId());
        workerUpdateInfo.setExecTaskNum(taskWorker.getExecTaskNum() == null ? 1 : taskWorker.getExecTaskNum() + 1);
        workerUpdateInfo.setLastExecTime(new Date());
        youji2TaskWorkerDao.updateById(workerUpdateInfo);
        // 3. 修改任务信息
        Youji2TaskInfo updateTask = new Youji2TaskInfo();
        updateTask.setId(taskInfo.getId());
        updateTask.setExecNum(taskInfo.getExecNum() == null ? 1 : taskInfo.getExecNum() + 1);
        updateTask.setLastExecTime(new Date());
        youji2TaskInfoDao.updateById(updateTask);
    }

    /**
     * 获取mgrIndex对应任务的工作节点
     *
     * @return
     */
    private List<Youji2TaskWorker> getTaskWorkerListByMgrIndex() {
        QueryWrapper<Youji2TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda().eq(Youji2TaskInfo::getBindMgrIndex, properties.getMgrIndex());
        List<Youji2TaskInfo> taskInfoList = youji2TaskInfoDao.list(taskInfoQueryWrapper);
        if (CollectionUtils.isEmpty(taskInfoList)) return null;

        List<String> taskNoList = taskInfoList.stream().map(ele -> ele.getTaskNo()).collect(Collectors.toList());
        QueryWrapper<Youji2TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
        taskWorkerQueryWrapper.lambda().in(Youji2TaskWorker::getTaskNo, taskNoList);
        return youji2TaskWorkerDao.list(taskWorkerQueryWrapper);
    }

    /**
     * 移除无效的工作节点
     */
    private void removeDieWorkerNode() {
        QueryWrapper<Youji2TaskWorker> taskWorkerDeleteQueryWrapper = new QueryWrapper<>();
        // 去掉10次 和10次以上的 心跳无响应的客户端
        taskWorkerDeleteQueryWrapper.lambda()
                .ge(Youji2TaskWorker::getBeatFailNum, properties.getHeartBeatFailNum());
        youji2TaskWorkerDao.remove(taskWorkerDeleteQueryWrapper);
    }

    /**
     * 获取所有任务数
     *
     * @return
     * @throws Exception
     */
    private Integer getAllTaskInfoNum() throws Exception {
        QueryWrapper<Youji2TaskInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Youji2TaskInfo::getTaskNo);
        return youji2TaskInfoDao.count(queryWrapper);
    }

    private Youji2TaskInfo saveTaskInfoToMysql(Youji2TaskRegisterParam param, Youji2TaskRegisterParam.YouJiTaskForm youJiTaskForm) throws Exception {

        // 所有可用的工作节点
        List<Youji2TaskMgr> mgrList = getEnableOnlineTaskMgr();
        // 所有的定时任务
        Integer mgrNum = CollectionUtils.isEmpty(mgrList) ? null : mgrList.size();

        // 检查taskNo 是否已经存在在数据库中 如果有则不再初始化。
        QueryWrapper<Youji2TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda().eq(Youji2TaskInfo::getTaskNo, youJiTaskForm.getTaskNo());
        Youji2TaskInfo taskInfo = youji2TaskInfoDao.getOne(taskInfoQueryWrapper);
        // 1. 保存到数据库中
        if (taskInfo == null) {
            // 如果没有则初始化任务信息到数据库中。
            taskInfo = new Youji2TaskInfo();
            taskInfo.setId(idWorker.nextId());
            Integer allTaskInfoNum = getAllTaskInfoNum();
            Integer bindMgrIndex = mgrNum == null ? null : mgrList.get(allTaskInfoNum % mgrNum).getMgrIndex();
            taskInfo.setBindMgrIndex(bindMgrIndex);
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
            if (BooleanUtils.isTrue(param.getIsWorkerRegister())) {
                taskInfo.setStartRegisterMgrIndex(properties.getMgrIndex());
            }
            taskInfo.setRegisterCompleteFlag(mgrList.size() == 1 ? Boolean.TRUE : Boolean.FALSE);
            taskInfo.setCreateTime(new Date());
            taskInfo.setUpdateTime(new Date());
            taskInfo.setLastExecTime(null);
            youji2TaskInfoDao.save(taskInfo);
        } else {
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
            youji2TaskInfoDao.updateById(taskInfo);
        }

        // 根据worker的ip port 查看worker表中是否已经有工作节点。
        QueryWrapper<Youji2TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
        taskWorkerQueryWrapper.lambda()
                .eq(Youji2TaskWorker::getTaskId, taskInfo.getId())
                .eq(Youji2TaskWorker::getWorkerIp, param.getWorkIP())
                .eq(Youji2TaskWorker::getWorkerPort, param.getWorkPort());
        Youji2TaskWorker taskWorker = youji2TaskWorkerDao.getOne(taskWorkerQueryWrapper);
        if (taskWorker == null) {
            taskWorker = new Youji2TaskWorker();
            taskWorker.setId(idWorker.nextId());
            taskWorker.setTaskId(taskInfo.getId());
            taskWorker.setTaskNo(taskInfo.getTaskNo());
            taskWorker.setWorkerServerName("");
            taskWorker.setWorkerIp(param.getWorkIP());
            taskWorker.setWorkerPort(param.getWorkPort());
            taskWorker.setEnableFlag(true);
            taskWorker.setOnlineFlag(true);
            taskWorker.setLastHeartBeatTime(new Date());
            youji2TaskWorkerDao.save(taskWorker);
        }

        return taskInfo;
    }

}
