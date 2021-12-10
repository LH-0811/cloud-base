package com.cloud.base.generator.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Map;

/**
 * @author lh0811
 * @date 2021/11/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    @ApiModelProperty("包路径")
    private String packageName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty(value = "类型映射")
    private Map<String,String> typeMap;

}
