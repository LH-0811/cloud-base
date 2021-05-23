package com.cloud.base.core.modules.lh_security.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.util.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.component.ProvideResToSecurityClient;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;
import com.cloud.base.core.modules.lh_security.client.entity.TokenToAuthorityParam;
import com.cloud.base.core.modules.lh_security.client.service.SecurityClientService;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/5/16
 */
@Slf4j
public class SecurityClientServiceImpl implements SecurityClientService {

    @Autowired
    private OkHttpClientUtil okHttpClientUtil;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProvideResToSecurityClient provideResToSecurityClient;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    /**
     * token 转换为用户权限封装信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    public SecurityAuthority tokenToSecurityAuthority(String token) throws Exception {

        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(), "为上传用户token,请登录后重试", null));
        }
        if (provideResToSecurityClient == null) {
            throw CommonException.create(ServerResponse.createByError("权限验证客户端，需要集成应用实现ProvideResToSecurityClient接口，为框架提供token和服务端地址"));
        }
        SecurityServerAddr serverAddr = provideResToSecurityClient.getServerAddrFromApplicationContext();
        Response response = okHttpClientUtil.postJSONParameters(serverAddr.toHttpAddrAndPort(), JSON.toJSONString(new TokenToAuthorityParam(token)));
        ServerResponse<SecurityAuthority> serverResponse = JSON.parseObject(response.body().string(), new TypeReference<ServerResponse<SecurityAuthority>>() {
        });
        log.debug("response:{}", JSON.toJSONString(serverResponse));
        if (!serverResponse.getStatus().equals(0))
            throw CommonException.create(serverResponse);
        return serverResponse.getData();
    }

    @Override
    /**
     * 判断用户是否有url权限
     *
     * @param token
     * @param url
     * @return
     * @throws Exception
     */
    public Boolean hasUrl(String token, String url) throws Exception {

        if (StringUtils.isBlank(url)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.ERR_AUTH.getCode(), "为上传Url，无法完成权限验证"));
        }

        // 获取到用户信息
        SecurityAuthority securityAuthority = tokenToSecurityAuthority(token);

        // 获取到所有的url权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
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
                return true;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
    }

    @Override
    /**
     * 判断用户是否有permsCode权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    public Boolean hasPermsCode(String token, String permsCode) throws Exception {
        if (StringUtils.isBlank(permsCode)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.ERR_AUTH.getCode(), "为上传PermsCode，无法完成权限验证"));
        }
        // 获取用户信息
        SecurityAuthority securityAuthority = tokenToSecurityAuthority(token);
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
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
                return true;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
    }

    @Override
    /**
     * 判断用户是有静态资源权限
     *
     * @param token
     * @param resPath
     * @return
     * @throws Exception
     */
    public Boolean hasStaticResPath(String token, String resPath) throws Exception {
        if (StringUtils.isBlank(resPath)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.ERR_AUTH.getCode(), "为上传ResPath，无法完成权限验证"));
        }
        // 获取用户信息
        SecurityAuthority securityAuthority = tokenToSecurityAuthority(token);
        // 获取到所有的权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的permsCode权限
        List<SecurityRes> staticResResList = securityResList.stream().filter(ele -> StringUtils.isNotBlank(ele.getPath())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResResList))
            throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
        // 获取到所有的code
        List<String> staticResPathList = staticResResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());

        for (String path : staticResPathList) {
            if (staticResPathList.contains(SecurityRes.ALL) || path.equalsIgnoreCase(resPath)) {
                return true;
            }
        }
        throw CommonException.create(ServerResponse.createByError(securityProperties.getUnAuthorizedCode(), "非法访问"));
    }


}
