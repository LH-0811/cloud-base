package com.cloud.base.member.user.repository.dao;

import com.cloud.base.member.user.repository.entity.SysRes;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 系统-资源表(SysRes)表数据库访问层
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
public interface SysResDao extends Mapper<SysRes>, SelectByIdListMapper<SysRes,Long> {


}
