package com.cloud.base.common.xugou.core.server.api.authentication;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;

/**
 * 权限校验
 *
 * @author lh0811
 * @date 2021/5/9
 */
public interface SecurityCheckAuthority {

    /**
     * 判断是否有url权限
     *
     * @param token
     * @param url
     * @return
     * @throws Exception
     */
    Boolean checkUrl(String token, String url) throws Exception;

    /**
     * 判断是否有permsCode权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    Boolean checkPermsCode(String token, String permsCode) throws Exception;

    /**
     * 判断是否有staticResPath权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    Boolean checkStaticResPath(String token, String permsCode) throws Exception;

    /**
     * 根据token 获取到用户权限信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    SecurityAuthority getSecurityAuthorityByToken(String token) throws Exception;
}
