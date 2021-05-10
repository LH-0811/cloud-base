
package com.cloud.base.core.modules.lh_security.server.network.base.http.component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyHttpServerHandler nettyHttpServerHandler;

    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());// http 编解码
        pipeline.addLast("httpAggregator",new HttpObjectAggregator(512*1024)); // http 消息聚合器                                                                     512*1024为接收的最大contentlength
        pipeline.addLast(nettyHttpServerHandler);// 请求处理器
    }
}
