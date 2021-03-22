package com.cloud.base.security.res_protect;

import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityResourceProtectAdapter;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源保护适配器
 * <p>
 * 通过该适配器
 * 告诉系统 哪些url指向的资源是收保护的 需要有相关权限的用户才可以访问
 * 并资源对应的是哪个权限码
 */
@Component
public class MyResProtectAdapter implements LhitSecurityResourceProtectAdapter {

    private List<String> protectUrls = null;

    @Override
    public List<String> getProtectUrlPatterns() {

        return Lists.newArrayList("/res/res1");
    }

}
