package com.cloud.base.member.property.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.PropCouponTemplateCreateParam;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import com.cloud.base.member.property.service.PropCouponUserService;
import com.cloud.base.member.property.service.PropScoreAccountService;
import com.cloud.base.member.property.vo.PropCouponOfUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api(tags = "优惠券资产 基本信息 通用接口")
@RestController
@RequestMapping("/prop/score_account/common")
public class CouponCommonController {

    @Autowired
    private PropScoreAccountService propScoreAccountService;

    @Autowired
    private PropCouponUserService propCouponUserService;

    /**
     * 创建 用户积分账户
     */
    @PostMapping("/init")
    @ApiOperation("创建 用户积分账户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropScoreAccountCreateParam", dataTypeClass = PropScoreAccountCreateParam.class, name = "param", value = "参数")
    })
    @TokenToAuthority
    public ServerResponse createPropScoreAccount(@RequestBody PropScoreAccountCreateParam param,@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        propScoreAccountService.createPropScoreAccount(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }


    /**
     * 获取当前用户优惠券列表
     */
    @PostMapping("/list")
    @ApiOperation("获取当前用户优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token")
    })
    @TokenToAuthority
    public ServerResponse<PropCouponOfUserVo> getCouponInfoOfUser(SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户优惠券列表 接口 : CouponController-couponTemplateCreate");
        PropCouponOfUserVo couponInfoOfUser = propCouponUserService.getCouponInfoOfUser(securityAuthority);
        return ServerResponse.createBySuccess("获取当前用户优惠券列表",couponInfoOfUser);
    }
}
