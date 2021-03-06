package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysRoleCreateParam;
import com.cloud.base.user.param.SysRoleQueryParam;
import com.cloud.base.user.param.SysRoleUpdateParam;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysResVo;
import com.cloud.base.user.vo.SysRoleVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色管理服务接口
 *
 * @author lh0811
 * @date 2021/8/17
 */
public interface SysRoleService {


// //////////////// 角色管理

    /**
     * 创建系统角色信息
     */
    void createRole(SysRoleCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 修改系统角色信息
     */
    void updateRole(SysRoleUpdateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除系统角色信息
     */
    void deleteRole(Long roleId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询系统角色信息
     */
    PageInfo<SysRoleVo> queryRole(SysRoleQueryParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取角色列表
     *
     * @return
     * @throws Exception
     */
    List<SysRoleVo> getRoleList(String roleName, SecurityAuthority securityAuthority) throws Exception;


    /**
     * 查询角色资源列表
     */
    List<SysResVo> getSysResListByRoleId(Long roleId,SecurityAuthority securityAuthority) throws Exception;


}
