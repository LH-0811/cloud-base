package com.cloud.base.example.cloud.account.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.cloud.account.api.ExampleAccountApi;
import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;
import com.cloud.base.example.cloud.account.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "账户接口")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    // value 资源名称 ， 降级后的处理
    @SentinelResource(value = "res_account_hello", blockHandler = "blockHandler")
    @GetMapping("/hello")
    @ApiOperation("hello测试流控接口")
    public ServerResponse hello() throws Exception {
        return ServerResponse.createBySuccess("hello account");
    }

    // 降级后的自定义返回
    public ServerResponse blockHandler(BlockException e) {
        return ServerResponse.createByError("账户系统hello：系统繁忙，请稍后再试");
    }


    @PostMapping("/subtraction")
    @ApiOperation("扣减余额")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "AccountSubtractionParam", dataTypeClass = AccountSubtractionParam.class, name = "param", value = "参数"),
    })
    public ServerResponse accountSubtraction(@Validated @RequestBody AccountSubtractionParam param) throws Exception {
        log.info(">>>>>>>>>>>扣减余额接口AccountController.accountSubtraction >>>>>>>>>>>>>>>>>>>>>>");
        accountService.accountSubtraction(param);
        return ServerResponse.createBySuccess("扣减成功");
    }

}
