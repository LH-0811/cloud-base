package com.cloud.base.example.cloud.account.api;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/3/25
 */
public interface ExampleAccountApi {

    @PostMapping("/account/subtraction")
    @ApiOperation("扣减余额")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "AccountSubtractionParam", dataTypeClass = AccountSubtractionParam.class, name = "param", value = "参数"),
    })
    ServerResponse accountSubtraction(@RequestBody AccountSubtractionParam param) throws Exception;
}
