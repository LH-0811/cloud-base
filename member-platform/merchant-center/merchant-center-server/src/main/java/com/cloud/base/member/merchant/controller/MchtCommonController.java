package com.cloud.base.member.merchant.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.memeber.merchant.param.MchtInfoCreateParam;
import com.cloud.base.memeber.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.memeber.merchant.vo.MchtInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api(tags = "商户基本信息通用接口")
@RestController
@RequestMapping("/merchant_base_info/common")
public class MchtCommonController {

    @Autowired
    private MchtInfoService mchtInfoService;


}
