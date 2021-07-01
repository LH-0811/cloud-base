package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.member.merchant.vo.MchtVipUserVo;
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
@Api(tags = "商户基本信息通用接口")
@RestController
@RequestMapping("/merchant_info/common")
public class MchtCommonController {

    @Autowired
    private MchtInfoService mchtInfoService;


    /**
     * 查询商户的会员用户列表
     */
    @PostMapping("/vip_user/list")
    @ApiOperation("查询商户的会员用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "UserOfMchtQueryParam", dataTypeClass = UserOfMchtQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_info/current_user/vip_user/list")
    public ServerResponse<PageInfo<SysUserVo>> getVipUserListOfMcht(@Validated @RequestBody UserOfMchtQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询商户的会员用户列表 接口 : MchtCurrentUserController-getVipUserListOfMcht");
        PageInfo<SysUserVo> pageInfo = mchtInfoService.getVipUserListOfMcht(param, securityAuthority);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

    /**
     * 用户加入到商户vip
     */
    @PostMapping("/join_mcht/{mchtId}")
    @ApiOperation("用户加入到商户vip")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "mchtId", value = "商户id")
    })
    @TokenToAuthority
    public ServerResponse joinToMchtVip(@PathVariable(value = "mchtId") Long mchtId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 用户加入到商户vip 接口 : MchtCurrentUserController-joinToMchtVip");
        mchtInfoService.joinToMchtVip(mchtId, securityAuthority);
        return ServerResponse.createBySuccess("操作成功");
    }

}
