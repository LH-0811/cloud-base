package com.cloud.base.member.user.expand.security.adapter;

import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityResourceProtectAdapter;
import com.cloud.base.member.user.repository.dao.SysResDao;
import com.cloud.base.member.user.repository.entity.SysRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 资源保护适配器
 * <p>
 * 通过该适配器
 * 告诉系统 哪些url指向的资源是收保护的 需要有相关权限的用户才可以访问
 */
@Component
public class MyResProtectAdapter implements LhitSecurityResourceProtectAdapter {

    private List<String> protectUrls = null;

    @Autowired
    private SysResDao sysResDao;

    @Override
    public List<String> getProtectUrlPatterns() {

        if (protectUrls == null) {
            List<SysRes> sysRes = sysResDao.selectAll();
            List<String> urls = sysRes.stream().map(ele -> ele.getUrl()).filter(ele-> StringUtils.isNotEmpty(ele)).collect(Collectors.toList());
            protectUrls = urls;
        }
        return protectUrls;
    }
}
