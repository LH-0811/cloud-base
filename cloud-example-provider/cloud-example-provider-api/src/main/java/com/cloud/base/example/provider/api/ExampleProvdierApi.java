package com.cloud.base.example.provider.api;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.example.provider.param.ProviderQueryParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/3/23
 */
public interface ExampleProvdierApi {

    @PostMapping("/provider/query")
    @ApiOperation("提供者查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "ProviderQueryParam", dataTypeClass = ProviderQueryParam.class, name = "param", value = "参数")
    })
    ServerResponse query(@RequestBody ProviderQueryParam param) throws Exception;

}
