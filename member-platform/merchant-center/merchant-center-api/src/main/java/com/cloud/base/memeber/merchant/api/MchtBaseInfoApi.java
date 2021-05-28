package com.cloud.base.memeber.merchant.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
public interface MchtBaseInfoApi {

    @PostMapping("/merchant_base_info/create11")
    @ApiOperation("创建商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "MchtBaseInfoCreateParam", dataTypeClass = MchtBaseInfoCreateParam.class, name = "param", value = "参数")
    })
    ServerResponse mchtBaseInfoCreate(@Validated @RequestBody MchtBaseInfoCreateParam param) throws Exception;

    @PostMapping("/merchant_base_info/query/by_user_id/{userId}")
    @ApiOperation("根据用户id 查询用户关联的商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "userId", value = "用户id")
    })
    public ServerResponse<List<MchtBaseInfoVo>> getMchtBaseInfoByUserId(@PathVariable(value = "userId") Long userId) throws Exception;
}
