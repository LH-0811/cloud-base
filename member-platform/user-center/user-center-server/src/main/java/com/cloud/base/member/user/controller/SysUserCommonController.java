package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.user.param.UserOfMchtQueryParam;
import com.cloud.base.member.user.service.SysUserCommonService;
import com.cloud.base.member.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户中心通用接口
 *
 * @author lh0811
 * @date 2021/6/9
 */
@Slf4j
@Api(tags = "用户中心-通用接口")
@RestController
@RequestMapping("/user_center/common")
public class SysUserCommonController {

    @Autowired
    private SysUserCommonService sysUserCommonService;


    /**
     * 查询商户的会员用户列表
     */
    @PostMapping("/mcht/user_list")
    @ApiOperation("查询商户的会员用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "UserOfMchtQueryParam", dataTypeClass = UserOfMchtQueryParam.class, name = "param", value = "参数")
    })
    @TokenToAuthority
    public ServerResponse<PageInfo<SysUserVo>> getUserVoListOfMcht(@Validated @RequestBody UserOfMchtQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询商户的会员用户列表 接口 : SysUserCommonController-getUserVoListOfMcht ");
        PageInfo<SysUserVo> userVoListOfMcht = sysUserCommonService.getUserVoListOfMcht(param);
        return ServerResponse.createBySuccess("查询成功", userVoListOfMcht);
    }

}
