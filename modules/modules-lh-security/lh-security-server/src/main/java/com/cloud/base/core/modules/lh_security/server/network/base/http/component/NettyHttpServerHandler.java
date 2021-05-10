package com.cloud.base.core.modules.lh_security.server.network.base.http.component;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.param.CheckResParam;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.server.network.base.util.HttpMsgUtil;
import com.cloud.base.core.modules.lh_security.server.network.server.SecurityCheckResServer;
import com.cloud.base.core.modules.lh_security.server.properties.SecurityServerProperties;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@ChannelHandler.Sharable
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Autowired
    private SecurityServerProperties securityServerProperties;

    @Autowired
    private SecurityCheckAuthority securityCheckAuthority;

    @Autowired
    private SecurityCheckResServer securityCheckResServer;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

        String reqUri = req.uri();
        CheckResParam checkResParam = null;
        try {
            String json = req.content().toString(CharsetUtil.UTF_8);
            checkResParam = JSONObject.parseObject(json, CheckResParam.class);
        } catch (Exception e) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getUnAuthorizedCode(), "参数格式错误"));
            return;
        }

        if (StringUtils.isBlank(checkResParam.getToken())) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getNoAuthorizedCode(), "未上传用户token"));
            return;
        }
        if (StringUtils.isBlank(checkResParam.getResource())) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityServerProperties.getUnAuthorizedCode(), "未上传要验证的资源"));
            return;
        }

        if (reqUri.equalsIgnoreCase(securityServerProperties.getServerUrlOfCheckUrl())) {
            securityCheckResServer.checkUrl(ctx,checkResParam.getToken(),checkResParam.getResource());
            return;
        } else if (reqUri.equalsIgnoreCase(securityServerProperties.getServerUrlOfCheckPermsCode())) {
            securityCheckResServer.checkPermsCode(ctx,checkResParam.getToken(),checkResParam.getResource());
            return;
        } else if (reqUri.equalsIgnoreCase(securityServerProperties.getServerUrlOfCheckStaticResPath())) {
            securityCheckResServer.checkStaticResPath(ctx,checkResParam.getToken(),checkResParam.getResource());
            return;
        }
        HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError("服务不存在"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (cause instanceof CommonException){
            CommonException exception = (CommonException) cause;
            HttpMsgUtil.sendResponse(ctx, exception.getServerResponse());
        }
        HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError("权限框架鉴权异常。"));
    }
}
