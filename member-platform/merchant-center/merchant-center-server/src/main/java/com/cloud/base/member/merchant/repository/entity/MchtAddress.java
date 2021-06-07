package com.cloud.base.member.merchant.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户中心-商户地址信息(MchtAddress)实体类
 *
 * @author lh0811
 * @since 2021-06-07 13:59:06
 */
@Setter
@Getter
@Table(name="mcht_address")
public class MchtAddress implements Serializable {

    /**
     * 商户地址id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="商户地址id")
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value="商户id")
    private Long mchtId;
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
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;


}
