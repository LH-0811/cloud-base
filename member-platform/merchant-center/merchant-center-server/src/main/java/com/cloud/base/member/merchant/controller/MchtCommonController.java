package com.cloud.base.member.merchant.controller;

import com.cloud.base.member.merchant.service.MchtInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "商户基本信息通用接口")
@RestController
@RequestMapping("/merchant_base_info/common")
public class MchtCommonController {

    @Autowired
    private MchtInfoService mchtInfoService;


}
