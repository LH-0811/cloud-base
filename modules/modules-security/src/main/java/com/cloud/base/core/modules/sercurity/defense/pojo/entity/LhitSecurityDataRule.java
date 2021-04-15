package com.cloud.base.core.modules.sercurity.defense.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 数据规则
 *
 * @author lh0811
 * @date 2021/4/15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LhitSecurityDataRule {

    // 角色ID
    private String roleId;

    // api地址
    private String apiPath;

    // 显示的字段列表
    private List<String> includeFields;

    // 排除的字段列表
    private List<String> excludeFields;

}
