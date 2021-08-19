package com.cloud.base.member.property.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.*;
import com.cloud.base.member.property.service.PropCouponMchtService;
import com.cloud.base.member.property.vo.PropCouponDetailVo;
import com.cloud.base.member.property.vo.PropCouponInfoVo;
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
@Api(tags = "优惠券资产 基本信息 商户管理接口")
@RestController
@RequestMapping("/prop/coupon/mcht")
public class CouponMchtController {

    @Autowired
    private PropCouponMchtService propCouponMchtService;


    /**
     * 创建优惠券模板
     */
    @PostMapping("/template/create")
    @ApiOperation("创建优惠券模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropCouponTemplateCreateParam", dataTypeClass = PropCouponTemplateCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse couponTemplateCreate(@Validated @RequestBody PropCouponTemplateCreateParam param, @ApiIgnore SecurityAuthority authority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建优惠券模板 接口 : CouponController-couponTemplateCreate");
        propCouponMchtService.couponTemplateCreate(param, authority);
        return ServerResponse.createBySuccess("创建优惠券模板完成");
    }

    /**
     * 修改优惠券模板
     */
    @PostMapping("/template/update")
    @ApiOperation("修改优惠券模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropCouponTemplateUpdateParam", dataTypeClass = PropCouponTemplateUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse couponTemplateUpdate(@Validated @RequestBody PropCouponTemplateUpdateParam param, @ApiIgnore SecurityAuthority authority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改优惠券模板 接口 : CouponController-couponTemplateUpdate");
        propCouponMchtService.couponTemplateUpdate(param, authority);
        return ServerResponse.createBySuccess("修改优惠券模板完成");
    }

    /**
     * 删除优惠券模板
     */
    @DeleteMapping("/template/delete/{templateId}")
    @ApiOperation("删除优惠券模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "templateId", value = "优惠券模板Id")
    })
    @HasUrl
    public ServerResponse couponTemplateDelete(@PathVariable(value = "templateId") Long templateId, @ApiIgnore SecurityAuthority authority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 修改优惠券模板 接口 : CouponController-couponTemplateDelete");
        propCouponMchtService.couponTemplateDelete(templateId, authority);
        return ServerResponse.createBySuccess("修改优惠券模板完成");

    }

    /**
     * 查询优惠券模板
     */
    @PostMapping("/template/query")
    @ApiOperation("查询优惠券模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropCouponTemplateQueryParam", dataTypeClass = PropCouponTemplateQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse<PageInfo<PropCouponTemplateVo>> couponTemplateQuery(@Validated @RequestBody PropCouponTemplateQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询优惠券模板 接口 : CouponController-couponTemplateQuery");
        PageInfo<PropCouponTemplateVo> pageInfo = propCouponMchtService.couponTemplateQuery(param, securityAuthority);
        return ServerResponse.createBySuccess("查询优惠券模板完成", pageInfo);
    }

    /**
     * 创建优惠券信息
     */
    @PostMapping("/info/create")
    @ApiOperation("创建优惠券信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropCouponInfoCreateParam", dataTypeClass = PropCouponInfoCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse couponInfoCreate(@Validated @RequestBody PropCouponInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建优惠券信息 接口 : CouponController-couponInfoCreate");
        propCouponMchtService.couponInfoCreate(param, securityAuthority);
        return ServerResponse.createBySuccess("创建优惠券信息完成");
    }

    /**
     * 查询优惠券信息列表
     */
    @PostMapping("/info/query")
    @ApiOperation("查询优惠券信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropCouponInfoCreateParam", dataTypeClass = PropCouponInfoCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse<PageInfo<PropCouponInfoVo>> couponInfoQuery(@Validated @RequestBody PropCouponInfoQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询优惠券信息列表 接口 : CouponController-couponInfoQuery");
        PageInfo<PropCouponInfoVo> pageInfo = propCouponMchtService.couponInfoQuery(param, securityAuthority);
        return ServerResponse.createBySuccess("查询优惠券信息列表完成", pageInfo);
    }

    /**
     * 优惠券消费
     */
    @PostMapping("/info/consume/{couponInfoId}")
    @ApiOperation("优惠券消费")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "couponInfoId", value = "优惠券信息Id")
    })
    @HasUrl
    public ServerResponse couponInfoConsume(@PathVariable(value = "couponInfoId") Long couponInfoId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 优惠券消费 接口 : CouponController-couponInfoConsume");
        propCouponMchtService.couponInfoConsume(couponInfoId, securityAuthority);
        return ServerResponse.createBySuccess("优惠券消费完成");
    }

    /**
     * 优惠券失效
     */
    @PostMapping("/info/invalid/{couponInfoId}")
    @ApiOperation("优惠券失效")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "couponInfoId", value = "优惠券信息Id")
    })
    @HasUrl
    public ServerResponse couponInfoInvalid(@PathVariable(value = "couponInfoId") Long couponInfoId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 优惠券失效 接口 : CouponController-couponInfoInvalid");
        propCouponMchtService.couponInfoInvalid(couponInfoId, securityAuthority);
        return ServerResponse.createBySuccess("优惠券失效完成");
    }

    /**
     * 获取优惠券详情
     */
    @GetMapping("/info/detail/{couponInfoId}")
    @ApiOperation("获取优惠券详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "couponInfoId", value = "优惠券信息Id")
    })
    @HasUrl
    public ServerResponse<PropCouponDetailVo> couponInfoDetailInfo(@PathVariable(value = "couponInfoId") Long couponInfoId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取优惠券详情 接口 : CouponController-couponInfoDetailInfo");
        PropCouponDetailVo detailVo = propCouponMchtService.couponInfoDetailInfo(couponInfoId, securityAuthority);
        return ServerResponse.createBySuccess("获取优惠券详情完成",detailVo);
    }


}
