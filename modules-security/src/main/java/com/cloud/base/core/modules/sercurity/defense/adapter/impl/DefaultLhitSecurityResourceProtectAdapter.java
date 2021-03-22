package com.cloud.base.core.modules.sercurity.defense.adapter.impl;

import com.google.common.collect.Lists;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityResourceProtectAdapter;

import java.util.List;

public class DefaultLhitSecurityResourceProtectAdapter implements LhitSecurityResourceProtectAdapter {


    @Override
    public List<String> getProtectUrlPatterns() {
        return Lists.newArrayList("/**");
    }

}
