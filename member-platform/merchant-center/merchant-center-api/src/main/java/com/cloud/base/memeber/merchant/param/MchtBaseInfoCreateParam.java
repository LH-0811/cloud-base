package com.cloud.base.memeber.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lh0811
 * @date 2021/5/27
 */
@Getter
@Setter
@ApiModel(description = "商户基本信息创建参数")
public class MchtBaseInfoCreateParam implements Serializable {


    /**
     * 商户用户id
     */
    @NotNull(message = "商户用户id不能为空")
    @ApiModelProperty(value="商户用户id",required = true)
    private Long mchtUserId;
    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不能为空")
    @ApiModelProperty(value="商户名称",required = true)
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


}
