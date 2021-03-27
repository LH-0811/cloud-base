package com.cloud.base.core.modules.sercurity.defense.adapter;


import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;

/**
 * 该适配器是为了方便 调用 userAuthentication tokenManager
 * 因为 用户提供凭证 完成认证后 一定要 将 token userinfo perms 三项保存到tokenManager中
 * 该适配器 通过从spring 容器中 获取到userAuthentication tokenManager 对象
 * 并完成了 调用userAuthentication 验证用户 和调用tokenManager来保存信息的过程
 * 相当于统一处理了两个步骤 避免使用过程中遗忘 保存信息到tokenManager中
 */
public interface LhitSecurityUserAuthenticationLoginAdapter {


    String userAuthenticationLogin(LhitSecurityUserVerification verification) throws Exception;
}
