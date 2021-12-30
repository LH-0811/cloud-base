package com.cloud.base.common.logger.adapter;

import java.util.List;

/**
 * 根据用户id获取到用户的角色信息
 *
 * @param <R> 用户角色
 * @param <U> 用户信息
 */
public interface LhitLoggerRoleInfoByUserAdapter<R, U> {
    List<R> getRoleInfoByUserInfo(U user);
}
