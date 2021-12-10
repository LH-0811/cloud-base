package com.cloud.base.modules.youji.code.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.modules.youji.code.repository.dao.TaskInfoDao;
import com.cloud.base.modules.youji.code.repository.dao.mapper.TaskInfoMapper;
import com.cloud.base.modules.youji.code.repository.entity.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * 定时任务表
 *
 * @author lh0811
 * @email lh0811
 * @date 2021-11-13 14:59:16
 */
@Service
public class TaskInfoDaoImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoDao {

}
