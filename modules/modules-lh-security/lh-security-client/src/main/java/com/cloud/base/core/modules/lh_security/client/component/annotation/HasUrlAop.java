package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.ProvideResToSecurityClient;
import com.cloud.base.core.modules.lh_security.client.util.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;
import com.cloud.base.core.modules.lh_security.client.entity.TokenToAuthorityParam;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class HasUrlAop {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasUrlAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasUrl annotation = signature.getMethod().getAnnotation(HasUrl.class);

        String url = annotation.url();
        if (StringUtils.isBlank(url))
            throw CommonException.create(ServerResponse.createByError("使用@HasUrl注解，必须填写url。"));

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
        // 获取到所有的url权限
        List<SecurityRes> securityResList = serverResponse.getData().getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的url权限
        List<SecurityRes> urlResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getUrl())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(urlResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的url
        List<String> urlMatcherList = urlResList.stream().map(ele -> ele.getUrl()).collect(Collectors.toList());
        for (String urlMatcher : urlMatcherList) {
            if (antPathMatcher.match(urlMatcher, url)) {
                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof SecurityAuthority)
                        args[i] = serverResponse.getData();
                }
                Object proceed = joinPoint.proceed(args);
                log.debug("退出HasUrlAop切面");
                return proceed;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
    }

}
