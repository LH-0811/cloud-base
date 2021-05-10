package com.cloud.base.core.modules.lh_security.server.authentication.impl;

import com.cloud.base.core.modules.lh_security.server.authentication.SecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/5/9
 */
@Slf4j
public class DefaultSecurityCheckAuthority implements SecurityCheckAuthority {

    @Autowired
    private TokenManager tokenManager;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 判断是否有url权限
     *
     * @param token
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public Boolean checkUrl(String token, String url) throws Exception {
        // 获取到token对应存储的用户权限信息
        SecurityAuthority securityAuthority = tokenManager.getSecurityAuthorityByToken(token);
        // 获取到所有的url权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList)) return false;
        // 获取到所有的url权限
        List<SecurityRes> urlResList = securityResList.stream().filter(ele -> ele.getResType().equals(SecurityRes.ResType.INTERFACE.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(urlResList)) return false;
        // 获取到所有的url
        List<String> urlMatcherList = urlResList.stream().map(ele -> ele.getUrl()).collect(Collectors.toList());
        for (String urlMatcher : urlMatcherList) {
            if (antPathMatcher.match(urlMatcher, url)) return true;
        }
        return false;
    }


    /**
     * 判断是否有permsCode权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    @Override
    public Boolean checkPermsCode(String token, String permsCode) throws Exception {
        // 获取到token对应存储的用户权限信息
        SecurityAuthority securityAuthority = tokenManager.getSecurityAuthorityByToken(token);
        // 获取到所有的permsCode权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList)) return false;
        // 获取到所有的permsCode权限
        List<SecurityRes> permsResList = securityResList.stream().filter(ele -> ele.getResType().equals(SecurityRes.ResType.PERMS_CODE.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permsResList)) return false;
        // 获取到所有的permsCode
        List<String> permsCodeList = permsResList.stream().map(ele -> ele.getCode()).collect(Collectors.toList());
        if (permsCodeList.contains("all") || permsCodeList.contains("ALL")) return true;
        for (String thisPermsCode : permsCodeList) {
            if (thisPermsCode.equals(permsCode)) return true;
        }
        return false;
    }



    /**
     * 判断是否有staticResPath权限
     *
     * @param token
     * @param staticResPath
     * @return
     * @throws Exception
     */
    @Override
    public Boolean checkStaticResPath(String token, String staticResPath) throws Exception {
        // 获取到token对应存储的用户权限信息
        SecurityAuthority securityAuthority = tokenManager.getSecurityAuthorityByToken(token);
        // 获取到所有的 StaticResPath 权限
        List<SecurityRes> securityResList = securityAuthority.getSecurityResList();
        if (CollectionUtils.isEmpty(securityResList)) return false;
        // 获取到所有的 StaticResPath 权限
        List<SecurityRes> staticResPathResList = securityResList.stream().filter(ele -> ele.getResType().equals(SecurityRes.ResType.STATIC_RES.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(staticResPathResList)) return false;
        // 获取到所有的 StaticResPath
        List<String> staticResPathList = staticResPathResList.stream().map(ele -> ele.getPath()).collect(Collectors.toList());
        if (staticResPathList.contains("all") || staticResPathList.contains("ALL")) return true;
        for (String thisStaticResPath : staticResPathList) {
            if (thisStaticResPath.equals(staticResPath)) return true;
        }
        return false;
    }

}
