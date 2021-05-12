package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.ProvideResToSecurityClient;
import com.cloud.base.core.modules.lh_security.client.component.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.entity.CheckResParam;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;
import com.cloud.base.core.modules.lh_security.client.entity.TokenToAuthorityParam;
import com.cloud.base.core.modules.lh_security.client.properties.SecurityClientProperties;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Aspect
@Slf4j
public class HasStaticResPathAop {

    @Autowired
    private SecurityClientProperties securityClientProperties;

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasStaticResPath)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasStaticResPathAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasStaticResPath annotation = signature.getMethod().getAnnotation(HasStaticResPath.class);

        String resPath = annotation.resPath();
        if (StringUtils.isBlank(resPath))
            throw CommonException.create(ServerResponse.createByError("使用@HasStaticResPath注解，必须填写resPath。"));


        String token = provideResToSecurityClient.getTokenFromApplicationContext();
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(securityClientProperties.getNoAuthorizedCode()), "未上传用户token", ""));
        }

        SecurityServerAddr serverAddr = provideResToSecurityClient.getServerAddrFromApplicationContext();
        String reqUrl = serverAddr.toHttpAddrAndPort() + securityClientProperties.getServerUrlOfGetSecurityAuthority();
        log.debug("获取到token:{}", token);
        log.debug("请求访问权限验证服务端地址:{}", reqUrl);
        Response response = okHttpClientUtil.postJSONParameters(reqUrl, JSON.toJSONString(new TokenToAuthorityParam(token)));
        ServerResponse<SecurityAuthority> serverResponse = JSON.parseObject(response.body().string(), new TypeReference<ServerResponse<SecurityAuthority>>() {
        });
        log.debug("response:{}", JSON.toJSONString(serverResponse));
        if (!serverResponse.getStatus().equals(0))
            throw CommonException.create(serverResponse);

        // 获取到所有的权限
        List<SecurityRes> securityResList = serverResponse.getData().getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityClientProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的permsCode权限
        List<SecurityRes> staticResResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getPath())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResResList))
            throw CommonException.create(ServerResponse.createByError(securityClientProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的code
        List<String> staticResPathList = staticResResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());

        for (String path : staticResPathList) {
            if (staticResPathList.contains(SecurityRes.ALL) || path.equalsIgnoreCase(resPath)) {
                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof SecurityAuthority)
                        args[i] = serverResponse.getData();
                }
                Object proceed = joinPoint.proceed(args);
                log.debug("退出HasStaticResPathAop切面");
                return proceed;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityClientProperties.getUnAuthorizedCode(), "非法访问"));

    }
}
