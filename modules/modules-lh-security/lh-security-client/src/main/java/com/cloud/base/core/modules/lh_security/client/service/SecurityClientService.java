package com.cloud.base.core.modules.lh_security.client.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;

/**
 * 权限组件服务接口
 *
 * @author lh0811
 * @date 2021/5/16
 */
public interface SecurityClientService {
    /**
     * token 转换为用户权限封装信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    SecurityAuthority tokenToSecurityAuthority(String token) throws Exception;

    /**
     * 判断用户是否有url权限
     *
     * @param token
     * @param url
     * @return
     * @throws Exception
     */
    Boolean hasUrl(String token, String url) throws Exception;

    /**
     * 判断用户是否有permsCode权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    Boolean hasPermsCode(String token, String permsCode) throws Exception;

    /**
     * 判断用户是有静态资源权限
     *
     * @param token
     * @param resPath
     * @return
     * @throws Exception
     */
    Boolean hasStaticResPath(String token, String resPath) throws Exception;
}
