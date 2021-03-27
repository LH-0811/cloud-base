package com.cloud.base.core.modules.logger.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestParam {
    private String key;
    private Object value;
}
