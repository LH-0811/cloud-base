package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.member.merchant.vo.MchtVipUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Api(tags = "商户基本信息通用接口")
@RestController
@RequestMapping("/merchant_base_info/common")
public class MchtCommonController {

    @Autowired
    private MchtInfoService mchtInfoService;


    /**
     * 获取商户的vip用户列表
     */
    @GetMapping("/join_vip/user_list/{mchtId}")
    @ApiOperation("获取商户的vip用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "mchtId", value = "商户id")
    })
    @TokenToAuthority
    public ServerResponse<List<MchtVipUserVo>> getVipUserOfMcht(@PathVariable Long mchtId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取商户的vip用户列表 接口 : MchtCommonController-getVipUserOfMcht");
        List<MchtVipUserVo> vipUserOfMcht = mchtInfoService.getVipUserOfMcht(mchtId);
        return ServerResponse.createBySuccess("操作成功", vipUserOfMcht);
    }
}
