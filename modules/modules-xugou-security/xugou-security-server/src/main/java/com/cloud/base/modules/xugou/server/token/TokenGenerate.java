package com.cloud.base.modules.xugou.server.token;

import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;

/**
 * token 生成器
 *
 * @author lh0811
 * @date 2021/5/8
 */
public interface TokenGenerate {

    String generate(SecurityAuthority authority) throws Exception;

}
