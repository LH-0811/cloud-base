package com.cloud.base.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.common.core.constant.CommonConstant;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.annotation.HasUrl;
import com.cloud.base.common.xugou.client.component.annotation.TokenToAuthority;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.api.UserCenterAuthorizeApi;
import com.cloud.base.user.api.UserCenterCurrentUserApi;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.CurrentUserService;
import com.cloud.base.user.service.SysUserService;
import com.cloud.base.user.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
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
@RequestMapping("/sys_user")
public class SysUserController  implements UserCenterAuthorizeApi, UserCenterCurrentUserApi {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private CurrentUserService currentUserService;

// ///////////////////////////////////用户操作
    /**
     * 创建用户
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/create")
    @ApiOperation("创建用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserCreateParam", dataTypeClass = SysUserCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse createUser(@Valid @RequestBody SysUserCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建用户 接口 : SysUserAdminController-createUser ");
        sysUserService.createUser(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 修改用户
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/update")
    @ApiOperation("修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUpdateParam", dataTypeClass = SysUserUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse updateUser(@Validated @RequestBody SysUserUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改用户 接口 : SysUserAdminController-updateUser ");
        sysUserService.updateUser(param, securityAuthority);
        return ServerResponse.createBySuccess("修改成功");
    }

    /**
     * 查询用户
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/query")
    @ApiOperation("查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserQueryParam", dataTypeClass = SysUserQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse<PageInfo<SysUserVo>> queryUser(@Validated @RequestBody SysUserQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询用户 接口 : SysUserAdminController-queryUser ");
        PageInfo<SysUserVo> pageInfo = sysUserService.queryUser(param, securityAuthority);
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{userId}")
    @ApiOperation("删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    @HasUrl
    public ServerResponse delUser(@PathVariable(value = "userId") Long userId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除用户 接口 : SysUserAdminController-delUser");
        sysUserService.delUser(userId, securityAuthority);
        return ServerResponse.createBySuccess("删除成功");
    }


    /**
     * 重置用户密码
     */
    @PostMapping("/reset/pwd")
    @ApiOperation("重置用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserResetPwdParam", dataTypeClass = SysUserResetPwdParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse resetPassword(@Validated @RequestBody SysUserResetPwdParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改用户 接口 : SysUserAdminController-resetPassword ");
        sysUserService.resetPassword(param.getUserId(), securityAuthority);
        return ServerResponse.createBySuccess("修改成功");
    }

// 当前用户

    @PostMapping("/verification/username_password")
    @ApiOperation("系统用户通过用户名密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerificationParam", dataTypeClass = UsernamePasswordVerificationParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SecurityAuthority> verificationUserByUsernameAndPwd(@Validated @RequestBody UsernamePasswordVerificationParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserCurrentUserController-getUesrInfo");
        SecurityAuthority securityAuthority = currentUserService.verification(param);
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current_user_prems")
    @ApiOperation("获取当前用户权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
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
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<UserInfoVo> getUserInfo(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserCurrentUserController-getUesrInfo");
        SysUserVo sysUser = currentUserService.getUserByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        List<MenuVo> menuTreeByUser = currentUserService.getMenuTreeByUser(sysUser.getId());
        List<SysResVo> resListByUser = currentUserService.getResListByUser(sysUser.getId());
        // 组织vo
        UserInfoVo userInfoVo = new UserInfoVo();
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser,sysUserVo);
        List<SysResVo> sysResVos = JSONArray.parseArray(JSON.toJSONString(resListByUser), SysResVo.class);
        userInfoVo.setUserInfo(sysUserVo);
        userInfoVo.setMenuTree(menuTreeByUser);
        userInfoVo.setMenuList(sysResVos);
        return ServerResponse.createBySuccess("获取成功",userInfoVo);
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
    public ServerResponse<List<SysRoleVo>> getUserRoleList(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户角色列表 接口 : SysUserCurrentUserController-getUserRoleList");
        List<SysRoleVo> roles = currentUserService.getUserRoleList(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        return ServerResponse.createBySuccess("查询成功", roles);
    }


    /**
     * 获取部门用户信息
     */
    @PostMapping("/dept/query")
    @ApiOperation("获取部门用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysDeptUserQueryParam", dataTypeClass = SysDeptUserQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse<PageInfo<DeptUserDto>> selectDeptUser(@Validated @RequestBody SysDeptUserQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取部门用户信息 接口 : SysDeptController-selectDeptUser ");
        PageInfo<DeptUserDto> deptUserDtoPageInfo = currentUserService.selectDeptUser(param, securityAuthority);
        return ServerResponse.createBySuccess("查询成功", deptUserDtoPageInfo);
    }


    /**
     * 获取用户菜单树
     *
     * @throws Exception
     */
    @GetMapping("/menu_tree/get")
    @ApiOperation("获取用户菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<List<MenuVo>> getMenuTreeByUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户资源树 接口 : SysUserCurrentUserController-getMenuTreeByUser");
        List<MenuVo> menuTreeByUser = currentUserService.getMenuTreeByUser(Long.valueOf(securityAuthority.getSecurityUser().getId()));
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
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    @TokenToAuthority
    public ServerResponse<List<SysResVo>> getResListByUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取用户资源列表 接口 : SysUserCurrentUserController-getResListByUser");
        List<SysResVo> resListByUser = currentUserService.getResListByUser(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        return ServerResponse.createBySuccess("查询成功", resListByUser);
    }

    /**
     * 用户修改密码
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/set/pwd")
    @ApiOperation("用户修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUpdatePasswordParam", dataTypeClass = SysUserUpdatePasswordParam.class, name = "param", value = "参数")
    })
    @TokenToAuthority
    public ServerResponse updateUserPassword(@Validated @RequestBody SysUserUpdatePasswordParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 用户修改密码 接口 : SysUserCurrentUserController-updateUserPassword");
        currentUserService.updateUserPassword(param, securityAuthority);
        return ServerResponse.createBySuccess("修改成功");
    }

}
