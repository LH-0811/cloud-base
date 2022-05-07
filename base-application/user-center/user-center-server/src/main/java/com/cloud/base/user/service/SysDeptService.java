package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysDeptVo;

import java.util.List;

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
    void createSysDept(SysDeptCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取部门树
     *
     * @return
     * @throws Exception
     */
    List<SysDeptVo> queryDeptTree(String deptName, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除部门信息
     */
    void deleteSysDept(Long deptId, SecurityAuthority securityAuthority) throws Exception;


    /**
     * 获取部门级联选项列表
     */
    List<SysDeptVo> queryDeptCascader(String deptName, SecurityAuthority securityAuthority) throws Exception;
}
