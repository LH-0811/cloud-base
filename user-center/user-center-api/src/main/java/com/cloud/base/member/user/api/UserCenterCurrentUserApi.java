package com.cloud.base.member.user.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.member.user.vo.SysUserVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface UserCenterCurrentUserApi {


    /**
     * 获取当前用户信息
     */
    @GetMapping("/user_center/current_user/current_user_info")
    @ApiOperation("获取当前基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    ServerResponse<SysUserVo> getUesrInfo() throws Exception;
}
