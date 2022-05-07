package com.cloud.base.user.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.constant.CommonConstant;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.annotation.HasUrl;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysRoleService;
import com.cloud.base.user.service.SysTenantInfoService;
import com.cloud.base.user.service.SysUserService;
import com.cloud.base.user.vo.SysRoleVo;
import com.cloud.base.user.vo.SysTenantInfoVo;
import com.cloud.base.user.vo.SysUserVo;
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

@Slf4j
@Api(tags = "用户中心-租户管理接口")
@RestController
@RequestMapping("/sys_tenant")
@HasUrl
public class SysTenantController  {

    @Autowired
    private SysTenantInfoService tenantInfoService;

    @Autowired
    private SysUserService sysUserService;

// ///////////////////////////////////角色管理

    /**
     * 创建租户信息
     */
    @PostMapping("/create")
    @ApiOperation("创建租户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysTenantInfoCreateParam", dataTypeClass = SysTenantInfoCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SysTenantInfoVo> tenantInfoCreate(@Validated @RequestBody SysTenantInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 创建租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("创建成功", tenantInfoService.tenantInfoCreate(param,securityAuthority));
    }

    /**
     * 删除租户信息(软删)
     */
    @DeleteMapping("/delete/{tenantInfoId}")
    @ApiOperation("删除租户信息(软删)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "tenantInfoId", value = "租户id")
    })
    public ServerResponse tenantInfoDelete(@PathVariable(value = "tenantInfoId") Long tenantInfoId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 删除租户信息 参数:{}", tenantInfoId);
        tenantInfoService.tenantInfoDelete(tenantInfoId, securityAuthority);
        return ServerResponse.createBySuccess("操作成功");
    }

    /**
     * 更新租户信息
     */
    @PostMapping("/update")
    @ApiOperation("更新租户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysTenantInfoUpdateParam", dataTypeClass = SysTenantInfoUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SysTenantInfoVo> tenantInfoUpdate(@Validated @RequestBody SysTenantInfoUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 更新租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("操作成功", tenantInfoService.tenantInfoUpdate(param, securityAuthority));
    }

    /**
     * 查询租户信息
     */
    @PostMapping("/query")
    @ApiOperation("查询租户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysTenantInfoUpdateParam", dataTypeClass = SysTenantInfoUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<SysTenantInfoVo>> tenantInfoQuery(@Validated @RequestBody SysTenantInfoQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 查询租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("操作成功", tenantInfoService.tenantInfoQuery(param, securityAuthority));
    }


    /**
     * 获取该租户的系统管理员
     */
    @GetMapping("/get_mgr_user/{tenantId}")
    @ApiOperation("获取该租户的系统管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "tenantId", value = "租户id")
    })
    public ServerResponse<SysUserVo> getTenantMgrUser(@PathVariable(value = "tenantId") Long tenantId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 创建该租户的系统管理员 参数:tenantId={}", tenantId);
        return ServerResponse.createBySuccess("获取成功，默认密码: " + UCConstant.DefaultPassword, tenantInfoService.getTenantMgrUser(tenantId, securityAuthority));
    }

    /**
     * 获取该租户的系统管理员
     */
    @PostMapping("/gen_mgr_user")
    @ApiOperation("获取该租户的系统管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "tenantId", value = "租户id")
    })
    public ServerResponse<SysUserVo> genTenantMgrUser(@Validated @RequestBody SysTenantMgrUserCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 创建该租户的系统管理员 参数:param={}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("获取成功，默认密码: " + UCConstant.DefaultPassword, tenantInfoService.genTenantMgrUser(param, securityAuthority));
    }

    @PostMapping("/mgr_user/reset_pwd")
    @ApiOperation("重置租户管理员密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserResetPwdParam", dataTypeClass = SysUserResetPwdParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse resetPassword(@Validated @RequestBody SysUserResetPwdParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("接口 [租户信息管理] 重置租户管理员密码 参数:param={}", JSON.toJSONString(param));
        sysUserService.resetPassword(param.getUserId(), securityAuthority);
        return ServerResponse.createBySuccess("修改成功");
    }

    @PostMapping("/mgr_user/update")
    @ApiOperation("更新租户管理员用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysTenantMgrUserUpdateParam", dataTypeClass = SysTenantMgrUserUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse updateTenantMgrUserInfo(@Validated @RequestBody SysTenantMgrUserUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("接口 [租户信息管理] 更新租户管理员用户信息 参数:param={}", JSON.toJSONString(param));
        tenantInfoService.updateTenantMgrUserInfo(param, securityAuthority);
        return ServerResponse.createBySuccess("修改成功");
    }
}
