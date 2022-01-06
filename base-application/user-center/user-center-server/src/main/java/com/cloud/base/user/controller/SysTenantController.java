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

@Slf4j
@Api(tags = "用户中心-租户管理接口")
@RestController
@RequestMapping("/sys_tenant")
@HasUrl
public class SysTenantController extends BaseController {

    @Autowired
    private SysTenantInfoService tenantInfoService;

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
    public ServerResponse<SysTenantInfo> tenantInfoCreate(@Validated @RequestBody SysTenantInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 创建租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("创建成功", tenantInfoService.tenantInfoCreate(param, getCurrentSysUser(securityAuthority)));
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
        tenantInfoService.tenantInfoDelete(tenantInfoId, getCurrentSysUser(securityAuthority));
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
    public ServerResponse<SysTenantInfo> tenantInfoUpdate(@Validated @RequestBody SysTenantInfoUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 更新租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("操作成功", tenantInfoService.tenantInfoUpdate(param, getCurrentSysUser(securityAuthority)));
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
    public ServerResponse<PageInfo<SysTenantInfo>> tenantInfoQuery(@Validated @RequestBody SysTenantInfoQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("接口 [租户信息管理] 查询租户信息 参数:{}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("操作成功", tenantInfoService.tenantInfoQuery(param, getCurrentSysUser(securityAuthority)));
    }

}
