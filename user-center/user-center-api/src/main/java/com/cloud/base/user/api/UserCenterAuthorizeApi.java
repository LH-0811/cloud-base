package com.cloud.base.user.api;

import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.UsernamePasswordVerificationParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface UserCenterAuthorizeApi {

    @PostMapping("/sys_user/verification/username_password")
    @ApiOperation("系统用户通过用户名密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerificationParam", dataTypeClass = UsernamePasswordVerificationParam.class, name = "param", value = "参数")
    })
    ServerResponse<SecurityAuthority> verificationUserByUsernameAndPwd(@Validated UsernamePasswordVerificationParam param) throws Exception;

}
