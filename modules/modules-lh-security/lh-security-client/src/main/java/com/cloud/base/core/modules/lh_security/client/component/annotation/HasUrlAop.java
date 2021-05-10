package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.GetTokenFromContext;
import com.cloud.base.core.modules.lh_security.client.component.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.entity.CheckResParam;
import com.cloud.base.core.modules.lh_security.client.properties.SecurityClientProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class HasUrlAop {

    @Autowired
    private SecurityClientProperties securityClientProperties;

    @Autowired
    private GetTokenFromContext getTokenFromContext;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl)")
    public void annotationPointCut() {
    }


    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) throws Exception {
        log.debug("进入HasUrlAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasUrl annotation = signature.getMethod().getAnnotation(HasUrl.class);
        String url = annotation.url();
        if (StringUtils.isBlank(url))
            throw CommonException.create(ServerResponse.createByError("使用@HasUrl注解，必须填写url。"));
        log.debug("拦截到的url:{}", url);
        String serverUrlOfCheckUrl = String.format("http://%s:%s%s", securityClientProperties.getServerAddr(), securityClientProperties.getServerPort(), securityClientProperties.getServerUrlOfCheckUrl());
        log.debug("请求访问权限验证服务端地址:{}", serverUrlOfCheckUrl);
        String token = getTokenFromContext.getToken();
        log.debug("获取到token:{}", token);
        Response response = okHttpClientUtil.postJSONParameters(serverUrlOfCheckUrl, JSON.toJSONString(new CheckResParam(token, url)));
        ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
        log.debug("response:{}", JSON.toJSONString(serverResponse));
        if (!serverResponse.getStatus().equals("0"))
            throw CommonException.create(serverResponse);
        log.debug("退出HasUrlAop切面");
    }

}
