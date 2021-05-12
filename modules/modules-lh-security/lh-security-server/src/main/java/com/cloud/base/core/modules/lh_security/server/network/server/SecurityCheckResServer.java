package com.cloud.base.core.modules.lh_security.server.network.server;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.server.network.base.util.HttpMsgUtil;
import com.cloud.base.core.modules.lh_security.server.properties.SecurityServerProperties;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对外公开 接口服务
 *
 * @author lh0811
 * @date 2021/5/10
 */
public class SecurityCheckResServer {

    @Autowired
    private SecurityServerProperties securityServerProperties;

    @Autowired
    private SecurityCheckAuthority securityCheckAuthority;

    public void checkUrl(ChannelHandlerContext ctx, String token, String resource) throws Exception {
        if (securityCheckAuthority.checkUrl(token, resource)) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("鉴权通过"));
        } else {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getUnAuthorizedCode(), "非法访问"));
        }
    }

    public void checkPermsCode(ChannelHandlerContext ctx, String token, String resource) throws Exception {
        if (securityCheckAuthority.checkPermsCode(token, resource)) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("鉴权通过"));
        } else {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getUnAuthorizedCode(), "非法访问"));
        }
    }

    public void checkStaticResPath(ChannelHandlerContext ctx, String token, String resource) throws Exception {
        if (securityCheckAuthority.checkStaticResPath(token, resource)) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("鉴权通过"));
        } else {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getUnAuthorizedCode(), "非法访问"));
        }
    }

    public void getSecurityAuthority(ChannelHandlerContext ctx, String token) throws Exception {
        SecurityAuthority securityAuthority = securityCheckAuthority.getSecurityAuthorityByToken(token);
        HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("获取成功",securityAuthority));
    }

}
