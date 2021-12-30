package com.cloud.base.user.api;

import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.user.vo.SysUserVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface UserCenterCurrentUserApi {


    /**
     * 获取当前用户信息
     */
    @GetMapping("/sys_user/current_user_info")
    @ApiOperation("获取当前基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    ServerResponse<SysUserVo> getUesrInfo() throws Exception;
}
