package com.cloud.base.member.merchant.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商户中心-商户vip用户关联表(MchtVipUser)实体类
 *
 * @author lh0811
 * @since 2021-06-08 22:10:40
 */
@Setter
@Getter
public class MchtVipUserVo implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value="商户id")
    private Long mchtId;
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;


}
