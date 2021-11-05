package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysDict;
import com.cloud.base.user.repository_plus.dao.SysDictDao;
import com.cloud.base.user.repository_plus.dao.SysDictDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 系统表-字典表(SysDict)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysDictDaoPlusImpl extends ServiceImpl<SysDictDao.SysDictMapper,SysDict> implements SysDictDaoPlus {

}