package com.cloud.base.core.modules.youji.code.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.core.modules.youji.code.repository.dao.YoujiTaskExecLogDao;
import com.cloud.base.core.modules.youji.code.repository.dao.mapper.YoujiTaskExecLogMapper;
import com.cloud.base.core.modules.youji.code.repository.entity.YoujiTaskExecLog;
import org.springframework.stereotype.Service;

/**
 * 定时任务表日志表
 *
 * @author lh0811
 * @email lh0811
 * @date 2021-11-17 20:34:07
 */
@Service
public class YoujiTaskExecLogDaoImpl extends ServiceImpl<YoujiTaskExecLogMapper, YoujiTaskExecLog> implements YoujiTaskExecLogDao {

}
