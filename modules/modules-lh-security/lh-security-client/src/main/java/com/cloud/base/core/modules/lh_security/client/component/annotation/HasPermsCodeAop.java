package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.ProvideResToSecurityClient;
import com.cloud.base.core.modules.lh_security.client.util.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;
import com.cloud.base.core.modules.lh_security.client.entity.TokenToAuthorityParam;
import com.cloud.base.core.modules.lh_security.client.service.SecurityClientService;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Aspect
@Slf4j
public class HasPermsCodeAop {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Autowired
    private SecurityClientService securityClientService;

    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasPermsCode)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasPermsCodeAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasPermsCode annotation = signature.getMethod().getAnnotation(HasPermsCode.class);
        String permsCode = annotation.permsCode();
        if (StringUtils.isBlank(permsCode))
            throw CommonException.create(ServerResponse.createByError("使用@HasPermsCode注解，必须填写permsCode。"));

        String token = provideResToSecurityClient.getTokenFromApplicationContext();
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(securityProperties.getNoAuthorizedCode()), "未上传用户token", ""));
        }


        SecurityServerAddr serverAddr = provideResToSecurityClient.getServerAddrFromApplicationContext();
        String reqUrl = serverAddr.toHttpAddrAndPort() + securityProperties.getServerUrlOfTokenToAuthority();
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
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的permsCode权限
        List<SecurityRes> permsResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permsResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的code
        List<String> codeList = permsResList.stream().map(ele -> ele.getCode()).collect(Collectors.toList());

        for (String code : codeList) {
            if (codeList.contains(SecurityRes.ALL) || code.equalsIgnoreCase(permsCode)) {
                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof SecurityAuthority)
                        args[i] = serverResponse.getData();
                }
                Object proceed = joinPoint.proceed(args);
                log.debug("退出HasPermsCodeAop切面");
                return proceed;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
    }

}
