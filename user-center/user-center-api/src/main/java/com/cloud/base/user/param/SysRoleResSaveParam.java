package com.cloud.base.user.param;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统角色查询 bean
 *
 * @auth lh0811
 * @date 2020/11/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleResSaveParam {


    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id",required = true)
    private Long roleId;

    @ApiModelProperty(value = "资源id")
    private List<Long> resIds= Lists.newArrayList();

}
