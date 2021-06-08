package com.cloud.base.member.merchant.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
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
 * 商户中心-商户信息表(MchtBaseInfo)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:05:58
 */
@Setter
@Getter
@ApiModel(description = "商户基本信息查询参数")
public class MchtInfoQueryParam implements Serializable {

    /**
     * 商户用户id
     */
    @ApiModelProperty(value="商户用户id")
    private Long mchtUserId;

    /**
     * 商户名称
     */
    @ApiModelProperty(value="商户名称")
    private String mchtName;

    /**
     * 是可用
     */
    @ApiModelProperty(value="是可用")
    private Boolean enableFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间（起）")
    private Date createTimeLow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间（止）")
    private Date createTimeUp;

    /**
     * 省编号
     */
    @ApiModelProperty(value="省编号")
    private String provinceCode;

    /**
     * 市编号
     */
    @ApiModelProperty(value="市编号")
    private String cityCode;

    /**
     * 区编号
     */
    @ApiModelProperty(value="区编号")
    private String areaCode;

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
     * 页码
     */
    @ApiModelProperty(value="页码")
    private Integer pageNum = CommonEntity.pageNum;

    /**
     * 每页条数
     */
    @ApiModelProperty(value="每页条数")
    private Integer pageSize = CommonEntity.pageSize;
}
