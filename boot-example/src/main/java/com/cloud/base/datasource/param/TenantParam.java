package com.cloud.base.datasource.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Getter
@Setter
public class TenantParam implements Serializable {

    /**
     * 租户id
     */
    @NotNull(message = "租户id不能为空")
    @ApiModelProperty(value = "租户id",required = true)
    private Long tenantId;
}
