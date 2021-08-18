package com.cloud.base.user.repository.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 用户中心-用户岗位信息关系表(SysUserPositionRelation)实体类
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sys_user_position_rel")
public class SysUserPositionRel implements Serializable {

    /**
     * 用户岗位关系id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="用户岗位关系id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 岗位id
     */
    @ApiModelProperty(value="岗位id")
    private Long positionId;


}
