package com.cloud.base.core.modules.lh_security.server.network.base.http.component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class NettyHttpSocketStart implements Runnable {

    @Autowired
    private NettyHttpServerInitializer nettyHttpServerInitializer;

    // 连接处理线程
    private static Integer bossNum = 1;

    // 工作线程
    private static Integer workerNum = 4;

    // http暴露端口
    private static Integer httpPort = 8999;

    @Override
    public void run() {
        // 链接处理线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossNum);
        // 工作线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerNum);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(nettyHttpServerInitializer);
            ChannelFuture f = bootstrap.bind(httpPort).sync();
            f.channel().closeFuture().sync();
            log.info(" Http 服务启动 : " + httpPort);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
