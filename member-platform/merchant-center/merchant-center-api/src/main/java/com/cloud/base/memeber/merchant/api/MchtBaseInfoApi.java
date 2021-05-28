package com.cloud.base.memeber.merchant.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author lh0811
 * @date 2021/5/28
 */
public interface MchtBaseInfoApi {

    /**
     * 创建商户基本信息
     */
    @PostMapping("/merchant_base_info/create")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtBaseInfoCreateParam", dataTypeClass = MchtBaseInfoCreateParam.class, name = "param", value = "参数")
    })
    ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtBaseInfoCreateParam param) throws Exception;
}
