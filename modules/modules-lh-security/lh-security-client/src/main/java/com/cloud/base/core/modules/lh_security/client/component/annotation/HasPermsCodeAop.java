package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.ProvideResToSecurityClient;
import com.cloud.base.core.modules.lh_security.client.component.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.entity.CheckResParam;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;
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


@Aspect
@Slf4j
public class HasPermsCodeAop {

    @Autowired
    private SecurityClientProperties securityClientProperties;

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasPermsCode)")
    public void annotationPointCut() {
    }


    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) throws Exception {
        log.debug("进入HasPermsCodeAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasPermsCode annotation = signature.getMethod().getAnnotation(HasPermsCode.class);

        String permsCode = annotation.permsCode();
        if (StringUtils.isBlank(permsCode)) throw CommonException.create(ServerResponse.createByError("使用@HasPermsCode注解，必须填写permsCode。"));

        String token = provideResToSecurityClient.getTokenFromApplicationContext();
        SecurityServerAddr serverAddr = provideResToSecurityClient.getServerAddrFromApplicationContext();
        String reqUrl = "http://" + serverAddr.toHttpAddrAndPort() + securityClientProperties.getServerUrlOfCheckPermsCode();

        log.debug("拦截到的permsCode:{}", permsCode);
        log.debug("获取到token:{}", token);
        log.debug("请求访问权限验证服务端地址:{}", reqUrl);

        Response response = okHttpClientUtil.postJSONParameters(reqUrl, JSON.toJSONString(new CheckResParam(token, permsCode)));
        ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
        log.debug("response:{}", JSON.toJSONString(serverResponse));
        if (!serverResponse.getStatus().equals("0"))
            throw CommonException.create(serverResponse);
        log.debug("退出HasPermsCodeAop切面");
    }

}
