package com.cloud.base.user.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/6/9
 */
@Getter
@Setter
public class UserOfMchtQueryParam {

    @ApiModelProperty(value = "商户id")
    private Long mchtId;

    @ApiModelProperty(value = "用户电话")
    private String phone;

    @ApiModelProperty(value = "创建时间(起)")
    private Date createTimeLow;

    @ApiModelProperty(value = "创建时间(止)")
    private Date createTimeUp;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = CommonEntity.pageSize;

}
