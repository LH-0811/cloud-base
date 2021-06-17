package com.cloud.base.member.property.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/6/17
 */
public interface PropScoreAccountApi {

    /**
     * 创建 用户积分账户
     */
    @PostMapping("/prop/score_account/common/init")
    @ApiOperation("创建 用户积分账户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "PropScoreAccountCreateParam", dataTypeClass = PropScoreAccountCreateParam.class, name = "param", value = "参数")
    })
    ServerResponse createPropScoreAccount(@RequestBody PropScoreAccountCreateParam param) throws Exception ;

}
