package com.cloud.base.common.logger.entity;

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
