package com.cloud.base.modules.logger.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AddressEntity {

    // ip 地址
    private String ip;

    // 物理地址
    private String address;
}
