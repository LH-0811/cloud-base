package com.cloud.base.user.controller;

import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.modules.xugou.client.component.annotation.HasUrl;
import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysRoleCreateParam;
import com.cloud.base.user.param.SysRoleQueryParam;
import com.cloud.base.user.param.SysRoleUpdateParam;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.service.SysRoleService;
import com.cloud.base.user.vo.SysRoleVo;
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
@Api(tags = "用户中心-角色管理接口")
@RestController
@RequestMapping("/sys_role")
@HasUrl
public class SysRoleController extends BaseController {


    @Autowired
    private SysRoleService sysRoleService;

// ///////////////////////////////////角色管理

    /**
     * 创建系统角色信息
     */
    @PostMapping("/create")
    @ApiOperation("创建系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleCreateParam", dataTypeClass = SysRoleCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse createRole(@Validated @RequestBody SysRoleCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建系统角色信息 接口 : SysAdminController-createRole ");
        sysRoleService.createRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 修改系统角色信息
     */
    @PostMapping("/update")
    @ApiOperation("修改系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleUpdateParam", dataTypeClass = SysRoleUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse updateRole(@Validated @RequestBody SysRoleUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改系统角色信息 接口 : SysAdminController-updateRole ");
        sysRoleService.updateRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("修改成功");
    }

    /**
     * 删除系统角色信息
     */
    @DeleteMapping("/delete/{roleId}")
    @ApiOperation("删除系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "roleId", value = "角色id")
    })
    public ServerResponse deleteRole(@PathVariable(name = "roleId") Long roleId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除系统角色信息 接口 : SysAdminController-deleteRole ");
        sysRoleService.deleteRole(roleId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询系统角色信息
     */
    @PostMapping("/query")
    @ApiOperation("查询系统角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysRoleQueryParam", dataTypeClass = SysRoleQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<SysRoleVo>> queryRole(@Validated @RequestBody SysRoleQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询系统角色信息 接口 : SysAdminController-queryRole ");
        PageInfo<SysRoleVo> pageInfo = sysRoleService.queryRole(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    /**
     * 获取角色列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/query/all_list")
    @ApiOperation("获取角色列表")
    public ServerResponse<List<SysRole>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取角色列表 接口 : SysAdminController-getRoleList ");
        List<SysRole> roleList = sysRoleService.getRoleList(roleName);
        return ServerResponse.createBySuccess("查询成功", roleList);
    }

    /**
     * 查询角色资源列表
     */
    @GetMapping("/res/by_id/{roleId}")
    @ApiOperation("查询角色资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "roleId", value = "角色Id")
    })
    public ServerResponse<List<SysRes>> getSysResListByRoleId(@PathVariable(name = "roleId") Long roleId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询角色资源列表 接口 : SysAdminController-getSysResListByRoleId ");
        List<SysRes> sysResList = sysRoleService.getSysResListByRoleId(roleId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("查询成功", sysResList);
    }


}
