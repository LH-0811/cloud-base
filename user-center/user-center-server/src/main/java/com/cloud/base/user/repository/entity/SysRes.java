package com.cloud.base.user.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统-资源表(SysRes)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
@Table(name = "sys_res")
public class SysRes implements Serializable {

    /**
     * 资源id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "资源id")
    private Long id;

    /**
     * 父节点id
     */
    @ApiModelProperty(value = "父节点id")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 类型：1-菜单 2-接口 3-权限码,4-静态资源
     */
    @ApiModelProperty(value = "类型：1-菜单 2-接口 3-权限码,4-静态资源")
    private Integer type;
    /**
     * 权限标识符
     */
    @ApiModelProperty(value = "权限标识符")
    private String code;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String url;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNo;
    /**
     * 是否是叶子节点
     */
    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;
    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String router;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    @Transient
    @ApiModelProperty(value = "子资源列表")
    private List<SysRes> children;

    @Transient
    @ApiModelProperty(value = "父节点")
    private SysRes parent;

    @Transient
    @ApiModelProperty(value = "标题")
    private String title;

    @Transient
    @ApiModelProperty(value = "标题")
    private String key;

    @Transient
    @ApiModelProperty(value = "标题")
    private String pkey;

    @Transient
    @ApiModelProperty(value = "是否选中")
    private Boolean checked;




    //：0-静态资源 1-目录 2-菜单 3-按钮 4-接口
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Type {
        GROUP(0, "菜单组"),
        MENU(1, "菜单"),
        INTERFACE(2, "接口"),
        PERMS_CODE(3, "权限码"),
        STATIC_RES(4, "静态资源");

        private Integer code;

        private String desc;


        public static String getDescByCode(Integer code) {
            Type[] typeEnums = values();
            for (Type type : typeEnums) {
                if (type.code.equals(code)) {
                    return type.desc;
                }
            }
            return null;
        }

        public static Integer getCodeByDesc(String desc) {
            Type[] typeEnums = values();
            for (Type type : typeEnums) {
                if (type.desc.equals(desc)) {
                    return type.code;
                }
            }
            return null;
        }
    }

}
