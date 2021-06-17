package com.cloud.base.member.merchant.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.merchant.feign.impl.PropScoreAccountApiClientFallbackFactory;
import com.cloud.base.member.property.api.PropScoreAccountApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/6/17
 */
@FeignClient(name = "property-center-server", contextId = "propScoreAccountApi", fallbackFactory = PropScoreAccountApiClientFallbackFactory.class, configuration = FeignConfiguration.class)
public interface PropScoreAccountApiClient extends PropScoreAccountApi {
}
