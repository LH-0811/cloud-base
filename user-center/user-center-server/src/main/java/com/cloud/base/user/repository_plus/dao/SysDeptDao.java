package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 用户中心-部门表(SysDept)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysDeptDao extends IService<SysDept> {
    @Repository
    public interface SysDeptMapper extends BaseMapper<SysDept> {

    }
}