package com.cloud.base.core.modules.rooster.util;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RoosterWorkOkHttpClientUtil {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置链接超时
            .writeTimeout(10, TimeUnit.SECONDS) // 设置写数据超时
            .readTimeout(30, TimeUnit.SECONDS) // 设置读数据超时
            .build();

    /**
     * 同步get请求
     */
    public Response syncGet(String url) throws Exception {
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        Response response = okHttpClient.newCall(request).execute(); // 返回实体
        return response;
    }


    /**
     * Post提交表单
     */
    public Response postFromParameters(String url, HashMap<String, String> bodyParams) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build(); // 表单键值对
        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求
        return okHttpClient.newCall(request).execute();
    }


    /**
     * Post提交JSON
     *
     */
    public Response postJSONParameters(String url, String json) throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MEDIA_TYPE, json)).build();
        return okHttpClient.newCall(request).execute();
    }


}
