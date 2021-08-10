package com.cloud.base.user.service;

import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.repository.entity.SysUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统部门服务接口
 *
 * @author lh0811
 * @date 2021/8/10
 */
public interface SysDeptService {
    /**
     * 创建部门 信息
     */
    void createSysDept(SysDeptCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除部门信息
     */
    void deleteSysDept(Long deptId, SysUser sysUser) throws Exception;
}
