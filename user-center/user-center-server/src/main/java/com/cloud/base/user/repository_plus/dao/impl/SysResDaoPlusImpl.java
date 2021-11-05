package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysRes;
import com.cloud.base.user.repository_plus.dao.SysResDao;
import com.cloud.base.user.repository_plus.dao.SysResDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 系统-资源表(SysRes)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysResDaoPlusImpl extends ServiceImpl<SysResDao.SysResMapper,SysRes> implements SysResDaoPlus {

}