package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysPosition;
import com.cloud.base.user.repository_plus.dao.SysPositionDao;
import com.cloud.base.user.repository_plus.dao.SysPositionDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 用户中心-岗位信息(SysPosition)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysPositionDaoPlusImpl extends ServiceImpl<SysPositionDao.SysPositionMapper,SysPosition> implements SysPositionDaoPlus {

}