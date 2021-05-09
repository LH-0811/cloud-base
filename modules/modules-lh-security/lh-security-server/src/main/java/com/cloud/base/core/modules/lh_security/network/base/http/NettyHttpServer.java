package com.cloud.base.core.modules.lh_security.network.base.http;

import com.cloud.base.core.modules.lh_security.network.base.http.component.NettyHttpSocketStart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于netty的http服务
 *
 * @author lh0811
 * @date 2021/5/9
 */
@Slf4j
public class NettyHttpServer implements CommandLineRunner {

    @Autowired
    private NettyHttpSocketStart nettyHttpSocketStart;

    private ExecutorService pool = Executors.newFixedThreadPool(2);

    @Override
    public void run(String... args) throws Exception {
        pool.submit(nettyHttpSocketStart);
        log.info("启动netty http服务成功");
    }
}
