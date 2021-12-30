package com.cloud.base.common.xugou.server.controller;

import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.core.model.param.CheckResParam;
import com.cloud.base.common.xugou.core.model.param.TokenParam;
import com.cloud.base.common.xugou.core.model.properties.XuGouSecurityProperties;
import com.cloud.base.common.xugou.core.server.api.authentication.SecurityCheckAuthority;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author lh0811
 * @date 2021/5/23
 */

@Slf4j
@ApiIgnore
@RestController
public class SecurityController {


    @Autowired
    private XuGouSecurityProperties xuGouSecurityProperties;

    @Autowired
    private SecurityCheckAuthority securityCheckAuthority;

    @PostMapping("${xugou.security.server-url-of-token-to-authority}")
    @ApiOperation(tags = "鉴权接口", value = "鉴权接口-token转换为用户权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CheckResParam", dataTypeClass = CheckResParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SecurityAuthority> tokenToAuthority(@RequestBody TokenParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 鉴权接口 token转换为用户权限信息 接口 : SecurityController-tokenToAuthority ");
        SecurityAuthority securityAuthority = securityCheckAuthority.getSecurityAuthorityByToken(param.getToken());
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }


}
