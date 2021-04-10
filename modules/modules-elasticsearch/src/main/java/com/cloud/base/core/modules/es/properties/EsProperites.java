package com.cloud.base.core.modules.es.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lh0811
 * @date 2021/4/10
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "es")
public class EsProperites {
}
