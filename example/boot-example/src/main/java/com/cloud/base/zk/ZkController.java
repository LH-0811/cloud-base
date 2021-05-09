package com.cloud.base.zk;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedBarrierEngine;
import com.cloud.base.core.modules.zk.distributed.function.subscribe.ZkStoryboardEngine;
import com.cloud.base.zk.service.barrier.BarrierService;
import com.cloud.base.zk.service.lock.ZkLockTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private ZkStoryboardEngine zkStoryboardEngine;

    @Autowired
    private BarrierService barrierService;

    @Autowired
    private DistributedBarrierEngine barrierEngine;

    //////////////////////////测试订阅者模式//////////////////////////////

    @GetMapping("/storyboard/{msg}")
    @ApiOperation("测试订阅者模式")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", dataTypeClass = String.class, name = "msg", value = "信息"),
    })
    public ServerResponse storyboard(@PathVariable("msg") String msg) throws Exception {
        zkStoryboardEngine.sendMsy("/cloud_base/zk_storyboard_default", msg);
        return ServerResponse.createBySuccess("测试完成");
    }

    //////////////////////////测试分布式锁//////////////////////////////
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

    @GetMapping("/lock_default_test")
    @ApiOperation("测试获取 zk 分布式锁使用")
    public ServerResponse testDefaultLock() throws Exception {

        Thread threadA = new Thread(() -> {
            log.info("线程A开始执行");
            try {
                zkLockTestService.testZkClient("A");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程A执行完成");
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            log.info("线程B开始执行");
            try {
                zkLockTestService.testZkClient("B");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程B执行完成");
        });
        threadB.start();


        return ServerResponse.createBySuccess("测试完成");
    }

    @GetMapping("/lock_one_res_test")
    @ApiOperation("测试获取两个方法 使用同一个资源锁 zk 分布式锁使用")
    public ServerResponse testOneResLock() throws Exception {

        Thread threadA = new Thread(() -> {
            log.info("线程A开始执行");
            try {
                zkLockTestService.testZkClient1("A");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程A执行完成");
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            log.info("线程B开始执行");
            try {
                zkLockTestService.testZkClient2("B");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程B执行完成");
        });
        threadB.start();


        return ServerResponse.createBySuccess("测试完成");
    }

    @GetMapping("/lock_diff_res_test")
    @ApiOperation("测试获取两个方法 使用同不同资源锁 zk 分布式锁使用")
    public ServerResponse testDiffResLock() throws Exception {

        Thread threadA = new Thread(() -> {
            log.info("线程A开始执行");
            try {
                zkLockTestService.testZkClient3("A");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程A执行完成");
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            log.info("线程B开始执行");
            try {
                zkLockTestService.testZkClient4("B");
            } catch (Exception e) {
                log.error(CommonMethod.getTrace(e));
            }
            log.info("线程B执行完成");
        });
        threadB.start();


        return ServerResponse.createBySuccess("测试完成");
    }

    //////////////////////////测试分布式栅栏//////////////////////////////
    @GetMapping("/barrier")
    @ApiOperation("测试分布式栅栏barrier")
    public ServerResponse barrier() throws Exception {
        log.info("开始准备线程");
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrierService.barrier((finalI + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        log.info("线程准备完成，准备执行方法");
        // 移除栅栏
        Thread.sleep(5000);
        barrierEngine.removeBarrier("/cloud_base/zk_distributed_barrier_default");
        return ServerResponse.createBySuccess("测试成功");
    }


    @GetMapping("/double_barrier")
    @ApiOperation("测试分布式栅栏doubleBarrier")
    public ServerResponse doubleBarrier() throws Exception {
        log.info("开始准备线程");
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            log.info("{}个线程准备好了",i);
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrierService.doubleBarrier(String.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        log.info("线程准备完成，准备执行方法");
        return ServerResponse.createBySuccess("测试成功");
    }

}
