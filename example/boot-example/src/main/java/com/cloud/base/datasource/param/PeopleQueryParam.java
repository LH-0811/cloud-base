package com.cloud.base.datasource.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Setter
@Getter
public class PeopleQueryParam extends TenantParam {
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = CommonEntity.pageSize;
}
