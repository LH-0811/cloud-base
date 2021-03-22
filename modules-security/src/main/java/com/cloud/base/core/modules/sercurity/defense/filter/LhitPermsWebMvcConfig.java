package com.cloud.base.core.modules.sercurity.defense.filter;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class LhitPermsWebMvcConfig implements WebMvcConfigurer {


    private LhitSecurityPermsFilter permsFilter;

    public LhitPermsWebMvcConfig(LhitSecurityPermsFilter permsFilter) {
        this.permsFilter = permsFilter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permsFilter).addPathPatterns("/**");
    }


}
