package com.cloud.base.common.logger.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LhitLoggerEntity<U, R> {

    private String title;

    private String msg;

    private List<RequestParam> paramList;

    private U userInfo;

    private R roleInfo;

    private AddressEntity addressEntity;

    private Object responseObj;

    private Throwable throwable;

}
