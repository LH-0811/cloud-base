package com.cloud.base.common.xugou.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.common.xugou.client.service.SecurityClient;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.core.model.entity.SecurityRes;
import com.cloud.base.common.xugou.core.model.param.TokenToAuthorityParam;
import com.cloud.base.common.xugou.core.model.properties.XuGouSecurityProperties;
import com.cloud.base.common.xugou.core.server.api.authentication.SecurityCheckAuthority;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

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
    private XuGouSecurityProperties xuGouSecurityProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RestTemplate restTemplate;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public SecurityAuthority tokenToAuthority(Boolean require) throws Exception {
        if (!require) {
            return new SecurityAuthority();
        }
        String token = provideResToSecurityClient.getTokenFromApplicationContext();
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getNoAuthorizedCode()), "未上传用户token", ""));
        }
        if (xuGouSecurityProperties.getUseCloud()) {
            String reqUrl = "http://" + xuGouSecurityProperties.getServerName() + xuGouSecurityProperties.getServerUrlOfTokenToAuthority();
            HttpEntity<TokenToAuthorityParam> request = new HttpEntity(new TokenToAuthorityParam(token));
            ServerResponse<SecurityAuthority> serverResponse = restTemplate.postForObject(reqUrl, request, ServerResponse.class);
            if (serverResponse.isSuccess()) {
                SecurityAuthority securityAuthority = JSON.parseObject(JSON.toJSONString(serverResponse.getData()), SecurityAuthority.class);
                return securityAuthority;
            } else {
                if (!require) {
                    return null;
                }
                throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getNoAuthorizedCode()), "token无效,请登录后重试", ""));
            }
        } else {
            SecurityCheckAuthority securityCheckAuthority = applicationContext.getBean(SecurityCheckAuthority.class);
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
        SecurityAuthority securityAuthority = tokenToAuthority(true);
        // 获取到所有的url权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url权限
        List<SecurityRes> urlResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getUrl())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(urlResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url
        List<String> urlMatcherList = urlResList.stream().map(ele -> ele.getUrl()).collect(Collectors.toList());
        for (String urlMatcher : urlMatcherList) {
            if (antPathMatcher.match(urlMatcher, url)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getUnAuthorizedCode()), "该用户没有权限访问当前资源", ""));
    }

    @Override
    public SecurityAuthority hasPermsCode(String permsCode) throws Exception {
        // 获取到当前权限信息
        SecurityAuthority securityAuthority = tokenToAuthority(true);
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> permsResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permsResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> codeList = permsResList.stream().map(ele -> ele.getCode()).collect(Collectors.toList());

        for (String code : codeList) {
            if (codeList.contains(SecurityRes.ALL) || code.equalsIgnoreCase(permsCode)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }

    @Override
    public SecurityAuthority hasStaticResPath(String resPath) throws Exception {
        // 获取到当前权限信息
        SecurityAuthority securityAuthority = tokenToAuthority(true);
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> staticResResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getPath())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> staticResPathList = staticResResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());
        for (String path : staticResPathList) {
            if (staticResPathList.contains(SecurityRes.ALL) || path.equalsIgnoreCase(resPath)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }

    @Override
    public SecurityAuthority tokenToAuthority(Boolean require, SecurityAuthority securityAuthority) throws Exception {
        return securityAuthority;
    }

    @Override
    public SecurityAuthority hasUrl(String url, SecurityAuthority securityAuthority) throws Exception {
        // 获取到所有的url权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url权限
        List<SecurityRes> urlResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getUrl())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(urlResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的url
        List<String> urlMatcherList = urlResList.stream().map(ele -> ele.getUrl()).collect(Collectors.toList());
        for (String urlMatcher : urlMatcherList) {
            if (antPathMatcher.match(urlMatcher, url)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getUnAuthorizedCode()), "该用户没有权限访问当前资源", ""));
    }

    @Override
    public SecurityAuthority hasPermsCode(String permsCode, SecurityAuthority securityAuthority) throws Exception {
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> permsResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permsResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> codeList = permsResList.stream().map(ele -> ele.getCode()).collect(Collectors.toList());

        for (String code : codeList) {
            if (codeList.contains(SecurityRes.ALL) || code.equalsIgnoreCase(permsCode)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }

    @Override
    public SecurityAuthority hasStaticResPath(String resPath, SecurityAuthority securityAuthority) throws Exception {
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的permsCode权限
        List<SecurityRes> staticResResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getPath())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResResList))
            throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
        // 获取到所有的code
        List<String> staticResPathList = staticResResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());
        for (String path : staticResPathList) {
            if (staticResPathList.contains(SecurityRes.ALL) || path.equalsIgnoreCase(resPath)) {
                return securityAuthority;
            }
        }
        throw CommonException.create(ServerResponse.createByError(xuGouSecurityProperties.getUnAuthorizedCode(), "该用户没有权限访问当前资源"));
    }


}
