package com.cloud.base.modules.security.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.common.exception.CommonException;
import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.modules.security.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.modules.security.client.service.SecurityClient;
import com.cloud.base.modules.security.core.entity.SecurityServerAddr;
import com.cloud.base.modules.security.core.param.TokenToAuthorityParam;
import com.cloud.base.modules.security.client.util.OkHttpClientUtil;
import com.cloud.base.modules.security.core.entity.SecurityAuthority;
import com.cloud.base.modules.security.core.entity.SecurityRes;
import com.cloud.base.modules.security.core.properties.SecurityProperties;
import com.cloud.base.modules.security.server.authentication.SecurityCheckAuthority;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端组件
 *
 * @author lh0811
 * @date 2021/5/24
 */
@Slf4j
public class DefaultSecurityClientImpl implements SecurityClient {

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Autowired
    private ApplicationContext applicationContext;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public SecurityAuthority tokenToAuthority() throws Exception {
        String token = provideResToSecurityClient.getTokenFromApplicationContext();
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(securityProperties.getNoAuthorizedCode()), "未上传用户token", ""));
        }
        SecurityServerAddr serverAddr = provideResToSecurityClient.getServerAddrFromApplicationContext();
        if (serverAddr.getIsCloud()) {
            String reqUrl = serverAddr.toHttpAddrAndPort() + securityProperties.getServerUrlOfTokenToAuthority();
            log.debug("获取到token:{}", token);
            log.debug("请求访问权限验证服务端地址:{}", reqUrl);
            Response response = okHttpClientUtil.postJSONParameters(reqUrl, JSON.toJSONString(new TokenToAuthorityParam(token)));
            ServerResponse<SecurityAuthority> serverResponse = JSON.parseObject(response.body().string(), new TypeReference<ServerResponse<SecurityAuthority>>() {
            });
            if (serverResponse.isSuccess()) {
                return serverResponse.getData();
            } else {
                throw CommonException.create(ServerResponse.createByError(Integer.valueOf(securityProperties.getNoAuthorizedCode()), "token无效,请登录后重试", ""));
            }
        } else {
            log.info("非Cloud应用,本地校验权限");
            SecurityCheckAuthority securityCheckAuthority = applicationContext.getBean(SecurityCheckAuthority.class);
            log.info("非Cloud应用,本地校验权限,获取到权限校验组件 SecurityCheckAuthority={}", securityCheckAuthority);
            if (securityCheckAuthority != null) {
                SecurityAuthority securityAuthority = securityCheckAuthority.getSecurityAuthorityByToken(token);
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError("token解析失败。"));
    }

    @Override
    public SecurityAuthority hasUrl(String url) throws Exception {
        // 获取到当前权限信息
        SecurityAuthority securityAuthority = tokenToAuthority();
        // 获取到所有的url权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url权限
        List<SecurityRes> urlResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getUrl())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(urlResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url
        List<String> urlMatcherList = urlResList.stream().map(ele -> ele.getUrl()).collect(Collectors.toList());
        for (String urlMatcher : urlMatcherList) {
            if (antPathMatcher.match(urlMatcher, url)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(Integer.valueOf(securityProperties.getUnAuthorizedCode()), "该用户没有权限访问当前资源", ""));
    }

    @Override
    public SecurityAuthority hasPermsCode(String permsCode) throws Exception {
        // 获取到当前权限信息
        SecurityAuthority securityAuthority = tokenToAuthority();
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> permsResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permsResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> codeList = permsResList.stream().map(ele -> ele.getCode()).collect(Collectors.toList());

        for (String code : codeList) {
            if (codeList.contains(SecurityRes.ALL) || code.equalsIgnoreCase(permsCode)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }

    @Override
    public SecurityAuthority hasStaticResPath(String resPath) throws Exception {
        // 获取到当前权限信息
        SecurityAuthority securityAuthority = tokenToAuthority();
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> staticResResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getPath())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> staticResPathList = staticResResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());
        for (String path : staticResPathList) {
            if (staticResPathList.contains(SecurityRes.ALL) || path.equalsIgnoreCase(resPath)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }


}
