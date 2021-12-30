package com.cloud.base.common.youji.component;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.entity.CommonMethod;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.youji.properties.YouJiWorkerProperties;
import com.cloud.base.common.youji.code.param.YouJiWorkerRegisterTaskParam;
import com.cloud.base.common.youji.code.util.YouJiOkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author lh0811
 * @date 2021/11/22
 */
@Slf4j
@Component
public class WorkerRegisterNetwork {

    @Autowired
    private YouJiWorkerProperties properties;

    @Autowired
    private YouJiOkHttpClientUtil httpClientUtil;

    // 消费线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    // 内存延时队列
    private DelayQueue<DelayedRegisterParamElement> delayQueue = new DelayQueue<>();


    @PostConstruct
    public void init() {
        // 启动消费线程
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    DelayedRegisterParamElement element = null;
                    try {
                        element = delayQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        log.info("获取消息失败:{}",CommonMethod.getTrace(e));
                    }
                    log.info("获取到延迟消息：{}", JSON.toJSONString(element));
                    registerToManage(element.getParam());
                }
            }
        });
    }

    public void registerToManage(YouJiWorkerRegisterTaskParam param) {
        try {
            String reqUrl = "http://" + properties.getManageHost() + ":" + properties.getManagePort() + "/youji/task/manage/create";
            Response response = httpClientUtil.postJSONParameters(reqUrl, JSON.toJSONString(param));
            if (response.code() == 200) { // http 请求响应200
                ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
                if (serverResponse.isSuccess()) { // 业务返回 处理成功
                    log.info("[酉鸡 注册服务到manager] 注册成功");
                    return; // 这时就处理完成了
                }
            }
            log.info("[酉鸡 注册服务到manager] response={}", response.body().string());
        } catch (Exception e) {
            log.info("[酉鸡 注册服务到manager] 失败:{}", CommonMethod.getTrace(e));
        }
        // 注册为成功的情景 将注册参数放入缓存 10秒钟时后 移除并重新发起请求，失败再次放入缓存直至成功
        delayQueue.put(new DelayedRegisterParamElement(10*1000, param));
    }


    public static class DelayedRegisterParamElement implements Delayed {

        private long delay; //延迟时间
        private long execTime; // 执行时间
        private YouJiWorkerRegisterTaskParam param;   //数据

        public YouJiWorkerRegisterTaskParam getParam() {
            return param;
        }

        public DelayedRegisterParamElement(long delay, YouJiWorkerRegisterTaskParam param) {
            this.delay = delay;
            this.param = param;
            this.execTime = System.currentTimeMillis() + delay;
        }

        /**
         * 需要实现的接口，获得延迟时间   用过期时间-当前时间
         *
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.execTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

    }

}
