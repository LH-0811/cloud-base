package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.dao.mapper.SysUserDeptRelMapper;
import com.cloud.base.user.repository_plus.entity.SysUserDeptRel;
import com.cloud.base.user.repository_plus.dao.SysUserDeptRelDao;
import org.springframework.stereotype.Service;

/**
 * 用户中心-用户部门信息关系表(SysUserDeptRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysUserDeptRelDaoPlusImpl extends ServiceImpl<SysUserDeptRelMapper,SysUserDeptRel> implements SysUserDeptRelDao {

}
