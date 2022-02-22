package com.cloud.base.generator.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author lh0811
 * @date 2022/1/21
 */
@Api(tags = "测试")
@Controller("/test")
public class TestController {

    @PostMapping("/receive")
    public void receiveReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. 缓存http请求
        ReqCache.save("test001", new HttpModel(request, response));

        // 2. 发送mq
        sendMq();
    }


    /**
     * 假设这个是简单消息处理完的通知方法
     *
     * @param notify
     */
    public void listener(Object notify) throws Exception {
        HttpModel httpModel = ReqCache.getByTxnId("test001");
        HttpServletResponse response = httpModel.getResponse();
        // response 输出到客户端
        PrintWriter writer = response.getWriter();
        writer.println("success");
        writer.flush();
        writer.close();
        // 移除缓存
        ReqCache.remove("test001");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HttpModel {
        private HttpServletRequest request;
        private HttpServletResponse response;
    }

    // 缓存
    public static class ReqCache {

        private static Cache<String, HttpModel> httpCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).removalListener(new RemovalListener<String, HttpModel>() {
            @Override
            public void onRemoval(RemovalNotification<String, HttpModel> notification) {
                try {
                    HttpServletResponse response = notification.getValue().getResponse();
                    PrintWriter writer = response.getWriter();
                    writer.println("timeout");
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).build();

        public static void save(String txnId, HttpModel httpModel) {
            httpCache.put(txnId, httpModel);
        }

        public static HttpModel getByTxnId(String txnId) {
            return httpCache.getIfPresent(txnId);
        }

        public static void remove(String txnId) {
            httpCache.invalidate(txnId);
        }
    }

    private void sendMq() {
    }
}


