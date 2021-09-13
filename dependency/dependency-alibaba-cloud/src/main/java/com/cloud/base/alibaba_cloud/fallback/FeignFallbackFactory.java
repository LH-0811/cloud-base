package com.cloud.base.alibaba_cloud.fallback;

import org.springframework.util.StringUtils;

/**
 * @author lh0811
 * @date 2021/5/28
 */
public class FeignFallbackFactory {


    public String errMsg(Throwable throwable) {
        String localizedMessage = throwable.getLocalizedMessage();
        StringBuilder sb = new StringBuilder();
        // 无可用服务
        if (localizedMessage.indexOf("does not have available server") > -1) {
            String[] split = StringUtils.split(localizedMessage, ":");
            sb.append("商户中心无可用服务:"+split[split.length-1]);
        }else {
            sb.append(localizedMessage);
        }
        return sb.toString();
    }
}
