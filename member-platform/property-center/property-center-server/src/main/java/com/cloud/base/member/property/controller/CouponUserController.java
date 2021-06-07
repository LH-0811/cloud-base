package com.cloud.base.member.property.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.*;
import com.cloud.base.member.property.service.PropCouponBaseService;
import com.cloud.base.member.property.service.PropCouponUserService;
import com.cloud.base.member.property.vo.PropCouponDetailVo;
import com.cloud.base.member.property.vo.PropCouponInfoVo;
import com.cloud.base.member.property.vo.PropCouponOfUserVo;
import com.cloud.base.member.property.vo.PropCouponTemplateVo;
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

/**
 * @author lh0811
 * @date 2021/6/4
 */
@Slf4j
@Api(tags = "优惠券资产 用户接口")
@RestController
@RequestMapping("/coupon/user")
public class CouponUserController {

    @Autowired
    private PropCouponUserService propCouponUserService;


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
