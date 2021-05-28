package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.repository.entity.MchtBaseInfo;
import com.cloud.base.member.merchant.service.MchtBaseInfoService;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoQueryParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoUpdateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
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

/**
 * 商户基本信息维护接口
 *
 * @author lh0811
 * @date 2021/5/26
 */
@Slf4j
@Api(tags = "商户基本信息维护接口")
@RestController
@RequestMapping("/merchant_base_info")
public class MchtBaseInfoController {

    @Autowired
    public MchtBaseInfoService mchtBaseInfoService;

    @PostMapping("/create")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtBaseInfoCreateParam", dataTypeClass = MchtBaseInfoCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/create")
    public ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtBaseInfoCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建商户基本信息 接口 : MchtBaseInfoController-mchtBaseInfoCreate");
        mchtBaseInfoService.mchtBaseInfoCreate(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    @PostMapping("/query")
    @ApiOperation("查询商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtBaseInfoQueryParam", dataTypeClass = MchtBaseInfoQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/query")
    public ServerResponse<PageInfo<MchtBaseInfoVo>> queryMchtBaseInfo(@Validated @RequestBody MchtBaseInfoQueryParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询商户基本信息 接口 : MchtBaseInfoController-queryMchtBaseInfo");
        PageInfo<MchtBaseInfoVo> pageInfo = mchtBaseInfoService.queryMchtBaseInfo(param);
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }

    @PostMapping("/update")
    @ApiOperation("更新商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtBaseInfoUpdateParam", dataTypeClass = MchtBaseInfoUpdateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/merchant_base_info/update")
    public ServerResponse updateMchtBaseInfo(@Validated @RequestBody MchtBaseInfoUpdateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 更新商户基本信息 接口 : MchtBaseInfoController-updateMchtBaseInfo");
        mchtBaseInfoService.updateMchtBaseInfo(param, securityAuthority);
        return ServerResponse.createBySuccess("更新成功");
    }

    @PostMapping("/query/by_user_id/{userId}")
    @ApiOperation("根据用户id 查询用户关联的商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    @HasUrl(url = "/merchant_base_info/query/by_user_id/{userId}")
    public ServerResponse<List<MchtBaseInfoVo>> getMchtBaseInfoByUserId(@PathVariable(value = "userId") Long userId,@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 根据用户id 查询用户关联的商户基本信息 接口 : MchtBaseInfoController-getMchtBaseInfoByUserId");
        List<MchtBaseInfoVo> mchtBaseInfoList = mchtBaseInfoService.getMchtBaseInfoByUserId(userId, securityAuthority);
        return ServerResponse.createBySuccess("查询成功",mchtBaseInfoList);
    }

}
