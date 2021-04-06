package com.cloud.base.zk;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import com.cloud.base.zk.service.ZkLockTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@RestController("/zk")
@Api(tags = "zookeeper测试接口")
public class ZkController {

    @Autowired
    private ZkDistributedClient zkClient;


    @Autowired
    private ZkLockTestService zkLockTestService;

    @GetMapping("/start_and_close")
    @ApiOperation("测试获取 关闭 重开zk客户端连接")
    public ServerResponse testStartOrClose() throws Exception {
        log.info("开始测试zk断开 再连接");

        // 测试连接
        zkClient.openZkConnection();
        Thread.sleep(1000);

        // 测试断开
        zkClient.closeClient();
        Thread.sleep(1000);

        // 重新连接
        zkClient.openZkConnection();
        Thread.sleep(1000);

        // 测试客户端是否可用
        List<String> strings = zkClient.getClient().getChildren().forPath("/");

        //  输出结果
        System.out.println(strings);

        return ServerResponse.createBySuccess("测试完成", strings);
    }


    @GetMapping("/lock_test")
    @ApiOperation("测试获取 zk 分布式锁使用")
    public ServerResponse testLock() throws Exception {

        Thread threadA = new Thread(()->{
            log.info("线程A开始执行");
            try {
                zkLockTestService.testZkClient("A");
            } catch (Exception e) {
                log.error( CommonMethod.getTrace(e));
            }
            log.info("线程A执行完成");
        });
        threadA.start();

        Thread threadB = new Thread(()->{
            log.info("线程B开始执行");
            try {
                zkLockTestService.testZkClient("B");
            } catch (Exception e) {
                log.error( CommonMethod.getTrace(e));
            }
            log.info("线程B执行完成");
        });
        threadB.start();


        return ServerResponse.createBySuccess("测试完成");
    }


}
