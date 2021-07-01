package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.feign.UserCenterCommonApiClient;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
import com.cloud.base.member.user.param.UserOfMchtQueryParam;
import com.cloud.base.member.user.vo.SysUserVo;
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
@Api(tags = "商户基本信息当前用户管理接口")
@RestController
@RequestMapping("/merchant_info/current_user")
public class MchtCurrentUserController {

    @Autowired
    private MchtInfoService mchtInfoService;


    /**
     * 创建商户基本信息
     */
    @PostMapping("/create")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtInfoCreateParam", dataTypeClass = MchtInfoCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_info/current_user/create")
    public ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建商户基本信息 接口 : MchtCurrentUserController-mchtBaseInfoCreate");
        mchtInfoService.mchtBaseInfoCreate(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 保存商户的福利配置
     */
    @PostMapping("/gift_settings/save")
    @ApiOperation("保存商户的福利配置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtGiftSettingsSaveParam", dataTypeClass = MchtGiftSettingsSaveParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/current_user/gift_settings/save")
    public ServerResponse saveMchtGiftSettings(@Validated @RequestBody MchtGiftSettingsSaveParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 保存商户的福利配置 接口 : MchtCurrentUserController-deletaMchtBaseInfo");
        mchtInfoService.saveMchtGiftSettings(param, securityAuthority);
        return ServerResponse.createBySuccess("保存成功");
    }


    /**
     * 获取当前用户关联商户列表
     */
    @PostMapping("/mcht_info_list")
    @ApiOperation("获取当前用户关联商户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token")
    })
    @HasUrl(url = "/merchant_info/current_user/mcht_info_list")
    public ServerResponse<List<MchtInfoVo>> getMchtInfoOfCurrentUser(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户关联商户列表 接口 : MchtCurrentUserController-getMchtInfoOfCurrentUser");
        List<MchtInfoVo> mchtInfoVoList = mchtInfoService.getMchtInfoOfCurrentUser(securityAuthority);
        return ServerResponse.createBySuccess("获取成功", mchtInfoVoList);
    }



}
