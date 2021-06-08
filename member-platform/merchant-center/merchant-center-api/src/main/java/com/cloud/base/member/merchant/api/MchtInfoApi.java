package com.cloud.base.member.merchant.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
public interface MchtInfoApi {

    @PostMapping("/merchant_base_info/manager/create")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtInfoCreateParam", dataTypeClass = MchtInfoCreateParam.class, name = "param", value = "参数")
    })
    ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtInfoCreateParam param) throws Exception;

    @PostMapping("/merchant_base_info/query/by_user_id/{userId}")
    @ApiOperation("根据用户id 查询用户关联的商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    ServerResponse<List<MchtInfoVo>> getMchtBaseInfoByUserId(@PathVariable(value = "userId") Long userId) throws Exception;



    /**
     * 获取商户基本信息
     */
    @GetMapping("/merchant_base_info/common/{mchtBaseInfoId}")
    @ApiOperation("获取商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "mchtBaseInfoId", value = "商户基本信息id")
    })
    ServerResponse<MchtInfoVo> getMchtBaseInfoVoById(@PathVariable(value = "mchtBaseInfoId") Long mchtBaseInfoId) throws Exception;


    /**
     * 用户加入到商户vip
     */
    @PostMapping("/merchant_base_info/current_user/join_vip/{mchtId}")
    @ApiOperation("用户加入到商户vip")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "mchtId", value = "商户id")
    })
    ServerResponse joinToMchtVip(@PathVariable(value = "mchtId") Long mchtId) throws Exception;
}
