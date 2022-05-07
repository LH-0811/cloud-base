package com.cloud.base.user.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.common.core.constant.CommonConstant;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.vo.MenuVo;
import com.cloud.base.user.vo.SysResVo;
import com.cloud.base.user.vo.SysUserVo;
import com.cloud.base.user.vo.UserInfoVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface UserCenterCurrentUserApi {



    @GetMapping("/current_user_info")
    @ApiOperation("获取当前基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    ServerResponse<UserInfoVo> getUserInfo(@ApiIgnore SecurityAuthority securityAuthority) throws Exception;
}
