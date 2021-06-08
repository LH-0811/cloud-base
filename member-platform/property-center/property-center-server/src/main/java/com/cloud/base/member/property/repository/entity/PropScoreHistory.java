package com.cloud.base.member.property.repository.entity;

import java.util.Date;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

/**
 * 资产-积分动账记录(PropSocreHistory)实体类
 *
 * @author lh0811
 * @since 2021-06-08 20:47:53
 */
@Setter
@Getter
@Table(name = "prop_score_history")
public class PropScoreHistory implements Serializable {

    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    private Long accountId;

    /**
     * 动账类型： 0-账户新建 1-充值 2-消费 3-赠与 4-冻结 5-解冻 9-其他
     */
    @ApiModelProperty(value = "动账类型： 0-账户新建 1-充值 2-消费 3-赠与 4-冻结 5-解冻 9-其他")
    private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String notes;
    /**
     * 动账积分
     */
    @ApiModelProperty(value = "动账积分")
    private Integer scoreChange;
    /**
     * 动账后积分
     */
    @ApiModelProperty(value = "动账后积分")
    private Integer scoreAfter;
    /**
     * 动账前积分
     */
    @ApiModelProperty(value = "动账前积分")
    private Integer scoreBefore;
    /**
     * 动账冻结积分
     */
    @ApiModelProperty(value = "动账冻结积分")
    private Integer frozenScoreChange;
    /**
     * 动账后冻结积分
     */
    @ApiModelProperty(value = "动账后冻结积分")
    private Integer frozenScoreAfter;
    /**
     * 动账前冻结积分
     */
    @ApiModelProperty(value = "动账前冻结积分")
    private Integer frozenScoreBefore;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum Type {


//         0-账户新建 1-充值 2-消费 3-赠与 4-冻结 5-解冻 9-其他
        INIT(0,"账户新建"),
        RECHARGE(1,"充值"),
        CONSUME(2,"消费"),
        GIVE(3,"赠送"),
        FROZEN(4,"冻结"),
        OTHER(9,"其他");

        private Integer code;


        private String msg;

    }
}
