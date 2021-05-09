package com.cloud.base.core.modules.lh_security.network.base.http.component;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.authentication.SecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.network.base.util.HttpMsgUtil;
import com.cloud.base.core.modules.lh_security.properties.SecurityProperties;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@ChannelHandler.Sharable
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SecurityCheckAuthority securityCheckAuthority;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

        String reqUri = req.uri();
        String token = req.headers().get(securityProperties.getTokenKey());
        if (StringUtils.isBlank(token)) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityProperties.getNoAuthorizedCode(), "未上传token，请登录后重试"));
        }

        String resource = null;
        try {
            String json = req.content().toString(CharsetUtil.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(json);
            resource = jsonObject.getString("resource");
        } catch (Exception e) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "参数格式错误"));
        }
        if (StringUtils.isBlank(resource)) {
            HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "未上传权限码"));
        }
        if (reqUri.equalsIgnoreCase(securityProperties.getServerUrlOfCheckUrl())) {
            if (securityCheckAuthority.checkUrl(token, resource)) {
                HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("鉴权通过"));
            } else {
                HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
            }
        } else if (reqUri.equalsIgnoreCase(securityProperties.getServerUrlOfCheckPermsCode())) {
            if (securityCheckAuthority.checkPermsCode(token, resource)) {
                HttpMsgUtil.sendResponse(ctx, ServerResponse.createBySuccess("鉴权通过"));
            } else {
                HttpMsgUtil.sendResponse(ctx, ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
            }
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
