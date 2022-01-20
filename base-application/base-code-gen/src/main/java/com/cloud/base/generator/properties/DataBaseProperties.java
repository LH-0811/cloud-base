package com.cloud.base.generator.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author lh0811
 * @date 2021/11/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "generator.datasource")
public class DataBaseProperties {

    @ApiModelProperty(value = "当前连接地址")
    private String url;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

}
