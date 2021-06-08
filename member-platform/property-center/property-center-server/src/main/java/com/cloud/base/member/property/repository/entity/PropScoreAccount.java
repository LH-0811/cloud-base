package com.cloud.base.member.property.repository.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 资产-用户积分账号(PropScoreAccount)实体类
 *
 * @author lh0811
 * @since 2021-06-08 20:47:53
 */
@Setter
@Getter
@Table(name="prop_score_account")
public class PropScoreAccount implements Serializable {

    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 商户id
     */
    @ApiModelProperty(value="商户id")
    private Long mchtId;
    /**
     * 可用积分
     */
    @ApiModelProperty(value="可用积分")
    private Integer score;
    /**
     * 冻结积分
     */
    @ApiModelProperty(value="冻结积分")
    private Integer frozenScore;
    /**
     * 是否可用 
     */
    @ApiModelProperty(value="是否可用 ")
    private Boolean enableFlag;
    /**
     * 是否删除
     */
    @ApiModelProperty(value="是否删除")
    private Boolean delFlag;
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