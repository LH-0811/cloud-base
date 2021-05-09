package com.cloud.base.core.modules.lh_security.token;

import com.cloud.base.core.modules.lh_security.entity.SecurityAuthority;

/**
 * token 生成器
 *
 * @author lh0811
 * @date 2021/5/8
 */
public interface TokenGenerate {

    String generate(SecurityAuthority authority) throws Exception;

}
