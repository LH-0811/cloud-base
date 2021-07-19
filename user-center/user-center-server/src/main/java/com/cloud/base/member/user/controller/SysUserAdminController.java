package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.param.*;
import com.cloud.base.member.user.service.SysAdminService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 系统管理员接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Slf4j
@Api(tags = "用户中心-系统管理员接口")
@RestController
@RequestMapping("/sys_admin")
public class SysUserAdminController extends BaseController {

    @Autowired
    private SysAdminService sysAdminService;

// ///////////////////////////////////用户操作

    /**
     * 创建用户
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/sys_user/create")
    @ApiOperation("创建用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserCreateParam", dataTypeClass = SysUserCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_user/create")
    public ServerResponse createUser(@Validated @RequestBody SysUserCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建用户 接口 : SysUserAdminController-createUser ");
        sysAdminService.createUser(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 修改用户
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/sys_user/update")
    @ApiOperation("修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUpdateParam", dataTypeClass = SysUserUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_user/update")
    public ServerResponse updateUser(@Validated @RequestBody SysUserUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改用户 接口 : SysUserAdminController-updateUser ");
        sysAdminService.updateUser(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("修改成功");
    }

    /**
     * 查询用户
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/sys_user/query")
    @ApiOperation("查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserQueryParam", dataTypeClass = SysUserQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_user/query")
    public ServerResponse<PageInfo<SysUser>> queryUser(@Validated @RequestBody SysUserQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询用户 接口 : SysUserAdminController-queryUser ");
        PageInfo<SysUser> pageInfo = sysAdminService.queryUser(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    /**
     * 设置用户角色列表
     */
    @PostMapping("/sys_user/roles/set")
    @ApiOperation("设置用户角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserRoleSetParam", dataTypeClass = SysUserRoleSetParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_user/roles/set")
    public ServerResponse setUserRoleList(@Validated @RequestBody SysUserRoleSetParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 设置用户角色列表 接口 : SysUserAdminController-setUserRegions");
        sysAdminService.setUserRoleList(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("设置成功");
    }


    /**
     * 删除用户
     */
    @DeleteMapping("/sys_user/delete/{userId}")
    @ApiOperation("删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    @HasUrl(url = "/sys_admin/sys_user/delete/{userId}")
    public ServerResponse delUser(@PathVariable(value = "userId") Long userId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除用户 接口 : SysUserAdminController-delUser");
        sysAdminService.delUser(userId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("删除成功");
    }

// ///////////////////////////////////资源管理

    /**
     * 创建权限
     */
    @PostMapping("/sys_res/create")
    @ApiOperation("创建权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysResCreateParam", dataTypeClass = SysResCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_res/create")
    public ServerResponse createRes(@Validated @RequestBody SysResCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建权限 接口 : SysAdminController-createRes ");
        sysAdminService.createRes(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除权限信息
     */
    @DeleteMapping("/sys_res/delete/{resId}")
    @ApiOperation("删除权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysResCreateParam", dataTypeClass = SysResCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_res/delete/{resId}")
    public ServerResponse deleteRes(@PathVariable(value = "resId") Long resId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除权限信息 接口 : SysAdminController-deleteRes ");
        sysAdminService.deleteRes(resId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("删除成功");
    }


    /**
     * 获取全部资源树
     */
    @PostMapping("/sys_res/tree")
    @ApiOperation("获取全部资源树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @HasUrl(url = "/sys_admin/sys_res/tree")
    public ServerResponse<List<SysRes>> getAllResTree(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取全部资源树 接口 : SysAdminController-getAllResTree ");
        List<SysRes> allResTree = sysAdminService.getAllResTree();
        return ServerResponse.createBySuccess("获取成功", allResTree);
    }

// ///////////////////////////////////角色管理

    /**
     * 创建系统角色信息
     */
    @PostMapping("/sys_role/create")
    @ApiOperation("创建系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleCreateParam", dataTypeClass = SysRoleCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_role/create")
    public ServerResponse createRole(@Validated @RequestBody SysRoleCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建系统角色信息 接口 : SysAdminController-createRole ");
        sysAdminService.createRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 修改系统角色信息
     */
    @PostMapping("/sys_role/update")
    @ApiOperation("修改系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleUpdateParam", dataTypeClass = SysRoleUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_role/update")
    public ServerResponse updateRole(@Validated @RequestBody SysRoleUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改系统角色信息 接口 : SysAdminController-updateRole ");
        sysAdminService.updateRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除系统角色信息
     */
    @DeleteMapping("/sys_role/delete/{roleId}")
    @ApiOperation("删除系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "roleId", value = "角色id")
    })
    @HasUrl(url = "/sys_admin/sys_role/delete/{roleId}")
    public ServerResponse deleteRole(@PathVariable(name = "roleId") Long roleId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除系统角色信息 接口 : SysAdminController-deleteRole ");
        sysAdminService.deleteRole(roleId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询系统角色信息
     */
    @PostMapping("/sys_role/query")
    @ApiOperation("查询系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleQueryParam", dataTypeClass = SysRoleQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_role/query")
    public ServerResponse<PageInfo<SysRole>> queryRole(@Validated @RequestBody SysRoleQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询系统角色信息 接口 : SysAdminController-queryRole ");
        PageInfo<SysRole> pageInfo = sysAdminService.queryRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    /**
     * 获取角色列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/sys_role/query/all_list")
    @ApiOperation("获取角色列表")
    @HasUrl(url = "/sys_admin/sys_role/query/all_list")
    public ServerResponse<List<SysRole>> getRoleList(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取角色列表 接口 : SysAdminController-getRoleList ");
        List<SysRole> roleList = sysAdminService.getRoleList();
        return ServerResponse.createBySuccess("查询成功", roleList);
    }

    /**
     * 保存角色权限
     */
    @PostMapping("/sys_role/save_res")
    @ApiOperation("保存角色资源（权限）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleResSaveParam", dataTypeClass = SysRoleResSaveParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_admin/sys_role/save_res")
    public ServerResponse saveRoleRes(@Validated @RequestBody SysRoleResSaveParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 保存角色资源（权限） 接口 : SysAdminController-queryRole ");
        sysAdminService.saveRoleRes(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("保存成功");
    }

    /**
     * 查询角色资源列表
     */
    @GetMapping("/sys_role/res/by_id/{roleId}")
    @ApiOperation("查询角色资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "roleId", value = "角色Id")
    })
    @HasUrl(url = "/sys_admin/sys_role/res/by_id/{roleId}")
    public ServerResponse<List<SysRes>> getSysResListByRoleId(@PathVariable(name = "roleId") Long roleId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询角色资源列表 接口 : SysAdminController-getSysResListByRoleId ");
        List<SysRes> sysResList = sysAdminService.getSysResListByRoleId(roleId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("查询成功", sysResList);
    }


}
