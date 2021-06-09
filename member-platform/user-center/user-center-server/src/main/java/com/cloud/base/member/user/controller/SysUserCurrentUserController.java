package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.user.param.SysUserRegisterParam;
import com.cloud.base.member.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserService;
import com.cloud.base.member.user.vo.MenuVo;
import com.cloud.base.member.user.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Api(tags = "用户中心-当前系统用户信息接口")
@RestController
@RequestMapping("/user_center/current_user")
public class SysUserCurrentUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;


    /**
     * 注册用户
     *
     * @throws Exception
     */
    @PostMapping("/register")
    @ApiOperation("注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserRegisterParam", dataTypeClass = SysUserRegisterParam.class, name = "param", value = "参数")
    })
    public ServerResponse registerUser(@Validated @RequestBody SysUserRegisterParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 注册用户 接口 : SysUserCurrentUserController-registerUser");
        sysUserService.registerUser(param);
        return ServerResponse.createBySuccess("注册成功");
    }

    @PostMapping("/verification/username_password")
    @ApiOperation("系统用户通过用户名密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerificationParam", dataTypeClass = UsernamePasswordVerificationParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SecurityAuthority> verificationUserByUsernameAndPwd(@Validated @RequestBody UsernamePasswordVerificationParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserCurrentUserController-getUesrInfo");
        SecurityAuthority securityAuthority = sysUserService.verification(param);
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current_user_prems")
    @ApiOperation("获取当前用户权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<SecurityAuthority> getUesrPremsInfo(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserCurrentUserController-getUesrInfo");
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current_user_info")
    @ApiOperation("获取当前基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<SysUserVo> getUesrInfo(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserCurrentUserController-getUesrInfo");
        SysUser sysUser = sysUserService.getUserByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser,sysUserVo);
        return ServerResponse.createBySuccess("获取成功",sysUserVo);
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
        log.info("进入 获取用户角色列表 接口 : SysUserCurrentUserController-getUserRoleList");
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
        log.info("进入 获取用户资源树 接口 : SysUserCurrentUserController-getResTreeByUser");
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
        log.info("进入 获取用户资源树 接口 : SysUserCurrentUserController-getMenuTreeByUser");
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
        log.info("进入 获取用户资源列表 接口 : SysUserCurrentUserController-getResListByUser");
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
        log.info("进入 用户修改密码 接口 : SysUserCurrentUserController-updateUserPassword");
        sysUserService.updateUserPassword(param, getCurrentSysUser(securityAuthority).getId());
        return ServerResponse.createBySuccess("修改成功");
    }
}
