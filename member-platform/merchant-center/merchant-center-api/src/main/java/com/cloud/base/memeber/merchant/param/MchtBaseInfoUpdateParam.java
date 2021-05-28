package com.cloud.base.memeber.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 商户中心-商户信息表(MchtBaseInfo)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:05:58
 */
@Setter
@Getter
@ApiModel(description = "商户基本信息更新参数")
public class MchtBaseInfoUpdateParam implements Serializable {

    /**
     * 商户信息主键
     */
    @NotNull(message = "商户信息id不能为空")
    @ApiModelProperty(value="商户信息主键",required = true)
    private Long id;

    /**
     * 商户名称
     */
    @ApiModelProperty(value="商户名称")
    private String mchtName;

    /**
     * 商户描述
     */
    @ApiModelProperty(value="商户描述")
    private String mchtDesc;

    /**
     * 商户地址
     */
    @ApiModelProperty(value="商户地址")
    private String address;

    /**
     * 是可用
     */
    @ApiModelProperty(value="是可用")
    private Boolean enableFlag;

}
