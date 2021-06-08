package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.merchant.param.MchtInfoQueryParam;
import com.cloud.base.member.merchant.param.MchtInfoUpdateParam;
import com.cloud.base.member.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
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
@Api(tags = "商户基本信息管理接口")
@RestController
@RequestMapping("/merchant_base_info/manager")
public class MchtAdminController {

    @Autowired
    public MchtInfoService mchtInfoService;

    @PostMapping("/create")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtInfoCreateParam", dataTypeClass = MchtInfoCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/manager/create")
    public ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建商户基本信息 接口 : MchtAdminController-mchtBaseInfoCreate");
        mchtInfoService.mchtBaseInfoCreate(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    @PostMapping("/query")
    @ApiOperation("查询商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtInfoQueryParam", dataTypeClass = MchtInfoQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/manager/query")
    public ServerResponse<PageInfo<MchtInfoVo>> queryMchtBaseInfo(@Validated @RequestBody MchtInfoQueryParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询商户基本信息 接口 : MchtAdminController-queryMchtBaseInfo");
        PageInfo<MchtInfoVo> pageInfo = mchtInfoService.queryMchtBaseInfo(param);
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    @PostMapping("/update")
    @ApiOperation("更新商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtInfoUpdateParam", dataTypeClass = MchtInfoUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/manager/update")
    public ServerResponse updateMchtBaseInfo(@Validated @RequestBody MchtInfoUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 更新商户基本信息 接口 : MchtAdminController-updateMchtBaseInfo");
        mchtInfoService.updateMchtBaseInfo(param, securityAuthority);
        return ServerResponse.createBySuccess("更新成功");
    }

    @PostMapping("/query/by_user_id/{userId}")
    @ApiOperation("根据用户id 查询用户关联的商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    @HasUrl(url = "/merchant_base_info/manager/query/by_user_id/{userId}")
    public ServerResponse<List<MchtInfoVo>> getMchtBaseInfoByUserId(@PathVariable(value = "userId") Long userId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 根据用户id 查询用户关联的商户基本信息 接口 : MchtAdminController-getMchtBaseInfoByUserId");
        List<MchtInfoVo> mchtBaseInfoList = mchtInfoService.getMchtBaseInfoByUserId(userId, securityAuthority);
        return ServerResponse.createBySuccess("查询成功", mchtBaseInfoList);
    }

    /**
     * 删除商户信息
     */
    @PostMapping("/delete/{mchtBaseId}")
    @ApiOperation("删除商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "mchtBaseId", value = "商户id")
    })
    @HasUrl(url = "/merchant_base_info/manager/delete/{mchtBaseId}")
    public ServerResponse deletaMchtBaseInfo(@PathVariable(value = "mchtBaseId") Long mchtBaseId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 根据用户id 查询用户关联的商户基本信息 接口 : MchtAdminController-deletaMchtBaseInfo");
        mchtInfoService.deletaMchtBaseInfo(mchtBaseId, securityAuthority);
        return ServerResponse.createBySuccess("删除成功");
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
    @HasUrl(url = "/merchant_base_info/manager/gift_settings/save")
    public ServerResponse saveMchtGiftSettings(@RequestBody MchtGiftSettingsSaveParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 保存商户的福利配置 接口 : MchtAdminController-deletaMchtBaseInfo");
        mchtInfoService.saveMchtGiftSettings(param, securityAuthority);
        return ServerResponse.createBySuccess("保存成功");
    }
}
