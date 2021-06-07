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
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lh0811
 * @date 2021/5/27
 */
@Getter
@Setter
@ApiModel(description = "商户基本信息创建参数")
public class MchtInfoCreateParam implements Serializable {


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
     * 省编号
     */
    @ApiModelProperty(value="省编号")
    private String provinceCode;
    /**
     * 省名称
     */
    @ApiModelProperty(value="省名称")
    private String provinceName;
    /**
     * 市编号
     */
    @ApiModelProperty(value="市编号")
    private String cityCode;
    /**
     * 市名称
     */
    @ApiModelProperty(value="市名称")
    private String cityName;
    /**
     * 区编号
     */
    @ApiModelProperty(value="区编号")
    private String areaCode;
    /**
     * 区名称
     */
    @ApiModelProperty(value="区名称")
    private String areaName;
    /**
     * 详细地址
     */
    @ApiModelProperty(value="详细地址")
    private String address;
    /**
     * 经度
     */
    @ApiModelProperty(value="经度")
    private BigDecimal longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value="纬度")
    private BigDecimal latitude;

}
