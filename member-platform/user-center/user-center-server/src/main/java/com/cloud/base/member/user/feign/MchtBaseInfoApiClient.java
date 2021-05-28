package com.cloud.base.member.user.feign;

import com.cloud.base.alibaba_cloud.FeignConfiguration;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.memeber.merchant.api.MchtBaseInfoApi;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@FeignClient(name = "merchant-center-server",contextId = "mchtBaseInfoApi",configuration = FeignConfiguration.class)
public interface MchtBaseInfoApiClient extends MchtBaseInfoApi {

}
