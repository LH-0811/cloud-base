package com.cloud.base.user.api;

import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.user.param.UserOfMchtQueryParam;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface UserCenterCommonApi {


    /**
     * 查询商户的会员用户列表
     */
    @PostMapping("/user_center/common/mcht/user_list")
    @ApiOperation("查询商户的会员用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "UserOfMchtQueryParam", dataTypeClass = UserOfMchtQueryParam.class, name = "param", value = "参数")
    })
    ServerResponse<PageInfo<SysUserVo>> getUserVoListOfMcht(@Validated @RequestBody UserOfMchtQueryParam param) throws Exception;
}
