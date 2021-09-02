package com.cloud.base.user.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/9/2
 */
@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/druid")
    public ServerResponse<MonitorVo> druid() {
        MonitorVo monitorVo = new MonitorVo();
        monitorVo.setDruidList(Lists.newArrayList());
        List<String> services = discoveryClient.getServices();
        log.info("注册中心中的服务列表:" + JSON.toJSONString(services));
        for (String serviceName : services) {
            DruidVo druidVo = new DruidVo();
            druidVo.setServiceName(serviceName);
            druidVo.setAddressList(Lists.newArrayList());
            List<ServiceInstance> instances = discoveryClient.getInstances("user-center-server");
            for (ServiceInstance instance : instances) {
                if (Boolean.valueOf(instance.getMetadata().get("is-druid"))) {
                    druidVo.getAddressList().add(new DruidVo.AddressVo(instance.getHost(), "http://"+instance.getHost()+":"+instance.getPort()+"/druid"));
                }
            }
            monitorVo.getDruidList().add(druidVo);
        }
        return ServerResponse.createBySuccess("测试完成", monitorVo);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonitorVo {
        @ApiModelProperty(value = "druid 监控列表")
        private List<DruidVo> druidList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DruidVo {
        @ApiModelProperty(value = "服务名")
        private String serviceName;

        @ApiModelProperty(value = "服务地址列表")
        private List<AddressVo> addressList;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AddressVo {
            @ApiModelProperty(value = "地址")
            private String host;
            @ApiModelProperty(value = "druid 监控地址")
            private String druidUrl;
        }
    }


}
