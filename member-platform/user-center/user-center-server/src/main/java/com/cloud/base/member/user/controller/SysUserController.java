package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.service.AuthorizeService;
import com.cloud.base.member.user.service.SysUserService;
import com.cloud.base.member.user.vo.MenuVo;
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
 * 系统用户接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Slf4j
@Api(tags = "当前系统用户信息接口")
@RestController
@RequestMapping("/sys_user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/verification/username_password")
    @ApiOperation("系统用户通过用户名密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerificationParam", dataTypeClass = UsernamePasswordVerificationParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SecurityAuthority> verificationUserByUsernameAndPwd(@Validated UsernamePasswordVerificationParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-getUesrInfo");
        SecurityAuthority securityAuthority = authorizeService.verification(param);
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }


    /**
     * 获取当前用户信息
     */
    @GetMapping("/current_user_prems")
    @ApiOperation("获取当前用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<SecurityAuthority> getUesrInfo(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-getUesrInfo");
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }

    /**
     * 获取用户角色列表
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/get/role_list")
    @ApiOperation("获取用户角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "参数")
    })
    @TokenToAuthority
    public ServerResponse<List<SysRole>> getUserRoleList(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户角色列表 接口 : SysUserController-getUserRoleList");
        List<SysRole> roles = sysUserService.getUserRoleList(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        return ServerResponse.createBySuccess("查询成功", roles);
    }

    /**
     * 获取用户资源树
     *
     * @throws Exception 异常
     */
    @PostMapping("/res_tree/get")
    @ApiOperation("获取用户资源树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<List<SysRes>> getResTreeByUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户资源树 接口 : SysUserController-getResTreeByUser");
        List<SysRes> resTreeByUser = sysUserService.getResTreeByUser(getCurrentSysUser(securityAuthority).getId());
        return ServerResponse.createBySuccess("查询成功", resTreeByUser);
    }

    /**
     * 获取用户菜单树
     *
     * @throws Exception
     */
    @PostMapping("/menu_tree/get")
    @ApiOperation("获取用户菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<List<MenuVo>> getMenuTreeByUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户资源树 接口 : SysUserController-getMenuTreeByUser");
        List<MenuVo> menuTreeByUser = sysUserService.getMenuTreeByUser(getCurrentSysUser(securityAuthority).getId());
        return ServerResponse.createBySuccess("查询成功", menuTreeByUser);
    }

    /**
     * 获取用户资源列表
     *
     * @throws Exception
     */
    @PostMapping("/res_list/get")
    @ApiOperation("获取用户资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<List<SysRes>> getResListByUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户资源列表 接口 : SysUserController-getResListByUser");
        List<SysRes> resListByUser = sysUserService.getResListByUser(getCurrentSysUser(securityAuthority).getId());
        return ServerResponse.createBySuccess("查询成功", resListByUser);
    }

    /**
     * 用户修改密码
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/re_set/pwd")
    @ApiOperation("用户修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUpdatePasswordParam", dataTypeClass = SysUserUpdatePasswordParam.class, name = "param", value = "参数")
    })
    @TokenToAuthority
    public ServerResponse updateUserPassword(@Validated @RequestBody SysUserUpdatePasswordParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 用户修改密码 接口 : SysUserController-updateUserPassword");
        sysUserService.updateUserPassword(param, getCurrentSysUser(securityAuthority).getId());
        return ServerResponse.createBySuccess("修改成功");
    }
}
