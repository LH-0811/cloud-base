package com.cloud.base.core.modules.youji.code.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskWorkerDao;
import com.cloud.base.core.modules.youji.code.repository.dao.mapper.TaskWorkerMapper;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import org.springframework.stereotype.Service;

/**
 * 定时任务的工作节点信息
 *
 * @author lh0811
 * @email lh0811
 * @date 2021-11-14 18:54:24
 */
@Service
public class TaskWorkerDaoImpl extends ServiceImpl<TaskWorkerMapper, TaskWorker> implements TaskWorkerDao {

}