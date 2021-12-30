package com.cloud.base.common.xugou.server.token;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;

/**
 * token 管理
 *
 * 从生成到最终销毁
 *
 * @author lh0811
 * @date 2021/5/7
 */
public interface TokenManager {


    /**
     * token 生成并存储
     *
     * @param securityAuthority
     * @return
     * @throws Exception
     */
    String tokenGenerateAndSave(SecurityAuthority securityAuthority) throws Exception;

    /**
     * 保存 用户 token
     *
     * @param token
     * @param authority
     */
    void saveToken(String token, SecurityAuthority authority) throws Exception;

    /**
     * 移除token
     *
     * @param token
     */
    void removeToken(String token) throws Exception;

    /**
     * 检查token是否可用
     *
     * @param token
     * @param userId
     * @return
     */
    Boolean checkToken(String token, String userId) throws Exception;

    /**
     * 根据用户token 获取用户信息
     *
     * @param token
     * @return
     */
    SecurityAuthority getSecurityAuthorityByToken(String token) throws Exception;

    /**
     * token 续期策略
     * @param token
     */
    void delayExpired(String token) throws Exception;

}
