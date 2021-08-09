package com.cloud.base.user.repository.dao;

import com.cloud.base.user.repository.entity.SysUser;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 系统-系统用户表(SysUser)表数据库访问层
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
public interface SysUserDao extends Mapper<SysUser>, SelectByIdListMapper<SysUser, Long> {


}
