package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysUserDeptRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 用户中心-用户部门信息关系表(SysUserDeptRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysUserDeptRelDao extends IService<SysUserDeptRel> {

}
