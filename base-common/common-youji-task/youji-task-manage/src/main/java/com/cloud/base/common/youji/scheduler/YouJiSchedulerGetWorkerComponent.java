package com.cloud.base.common.youji.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.youji.code.constant.YouJiConstant;
import com.cloud.base.common.youji.code.dto.GetWorkerDto;
import com.cloud.base.common.youji.code.exception.YouJiException;
import com.cloud.base.common.youji.code.repository.dao.TaskInfoDao;
import com.cloud.base.common.youji.code.repository.dao.TaskWorkerDao;
import com.cloud.base.common.youji.code.repository.entity.TaskInfo;
import com.cloud.base.common.youji.code.repository.entity.TaskWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Slf4j
@Component
public class YouJiSchedulerGetWorkerComponent {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskWorkerDao taskWorkerDao;

    /**
     * 更加任务编号获取工作节点
     *
     * @param taskNo
     * @return
     * @throws YouJiException
     */
    public GetWorkerDto getTaskWorkerByTaskNo(String taskNo) throws YouJiException {
        GetWorkerDto getWorkerDto = new GetWorkerDto();
        QueryWrapper<TaskInfo> taskInfoQueryWrapper = new QueryWrapper<>();
        taskInfoQueryWrapper.lambda().eq(TaskInfo::getTaskNo, taskNo);
        TaskInfo taskInfo = taskInfoDao.getOne(taskInfoQueryWrapper);
        if (taskInfo == null) {
            throw new YouJiException(YouJiConstant.YouJiErrorEnum.NOT_EXIST_TASK, taskNo);
        }
        getWorkerDto.setTaskInfo(taskInfo);
        // 找到目标客户端端
        if (YouJiConstant.ExecType.SINGLE_NODE.getCode().equals(taskInfo.getExecType())) {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型:{} 进入单节点执行发布流程", taskInfo.getTaskNo(), taskInfo.getExecType());
            // todo liuhe 获取到最优的工作节点 (算法待优化 先拿执行次数最少的节点)
            TaskWorker taskWorker = getSingleNode(taskInfo);
            getWorkerDto.setTaskWorkerList(Lists.newArrayList(taskWorker));
            return getWorkerDto;
        } else if (YouJiConstant.ExecType.ALL_NODE.getCode().equals(taskInfo.getExecType())) {
            List<TaskWorker> allNode = getAllNode(taskInfo);
            getWorkerDto.setTaskWorkerList(allNode);
            return getWorkerDto;
        } else {
            log.info("[酉鸡 Manage向Worker 发起任务] taskNo:{} 对应的执行类型不合法:{}", taskInfo.getTaskNo(), taskInfo.getExecType());
            throw new YouJiException(YouJiConstant.YouJiErrorEnum.TASK_EXEC_TYPE_ERR, taskInfo, "ExecType=" + taskInfo.getExecType());
        }
    }


    private TaskWorker getSingleNode(TaskInfo taskInfo) throws YouJiException {
        log.info("[YouJi-Manage 挑选一个工作节点] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
        taskWorkerQueryWrapper.lambda()
                .eq(TaskWorker::getTaskId, taskInfo.getId())
                .eq(TaskWorker::getTaskNo, taskInfo.getTaskNo())
                .eq(TaskWorker::getBeatFailNum, 0)
                .eq(TaskWorker::getEnableFlag, Boolean.TRUE)
                .eq(TaskWorker::getOnlineFlag, Boolean.TRUE)
                .orderByAsc(TaskWorker::getExecTaskNum);
        List<TaskWorker> taskWorkerList = taskWorkerDao.list(taskWorkerQueryWrapper);
        if (CollectionUtils.isEmpty(taskWorkerList)) {
            throw new YouJiException(YouJiConstant.YouJiErrorEnum.NOT_FIND_WORKER, taskInfo, "");
        }
        return taskWorkerList.get(0);
    }

    private List<TaskWorker> getAllNode(TaskInfo taskInfo) {
        log.info("[YouJi-Manage 获取全部可用的工作节点] date={}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<TaskWorker> taskWorkerQueryWrapper = new QueryWrapper<>();
        taskWorkerQueryWrapper.lambda()
                .eq(TaskWorker::getTaskId, taskInfo.getId())
                .eq(TaskWorker::getTaskNo, taskInfo.getTaskNo())
                .eq(TaskWorker::getBeatFailNum, 0)
                .eq(TaskWorker::getEnableFlag, Boolean.TRUE)
                .eq(TaskWorker::getOnlineFlag, Boolean.TRUE)
                .orderByAsc(TaskWorker::getExecTaskNum);
        return taskWorkerDao.list(taskWorkerQueryWrapper);
    }

}
