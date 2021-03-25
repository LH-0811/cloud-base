package com.cloud.base.example.cloud.account.controller;


import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;
import com.cloud.base.example.cloud.account.service.AccountService;
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

@Slf4j
@Api(tags = "账户接口")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/subtraction")
    @ApiOperation("扣减余额")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "AccountSubtractionParam", dataTypeClass = AccountSubtractionParam.class, name = "param", value = "参数"),
    })
    public ServerResponse accountSubtraction(@RequestBody AccountSubtractionParam param) throws Exception {
        log.info(">>>>>>>>>>>扣减余额接口AccountController.accountSubtraction >>>>>>>>>>>>>>>>>>>>>>");
        accountService.accountSubtraction(param);
        return ServerResponse.createBySuccess("扣减成功");
    }

}
