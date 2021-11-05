package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysDept;
import com.cloud.base.user.repository_plus.dao.SysDeptDao;
import com.cloud.base.user.repository_plus.dao.SysDeptDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 用户中心-部门表(SysDept)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysDeptDaoPlusImpl extends ServiceImpl<SysDeptDao.SysDeptMapper,SysDept> implements SysDeptDaoPlus {

}